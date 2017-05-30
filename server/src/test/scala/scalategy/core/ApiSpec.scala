package scalategy.core

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{TestActors, TestKit}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scalategy.ServerContextLike
import scalategy.shared._

class ApiSpec extends TestKit(ActorSystem("my-system")) with FlatSpecLike with Matchers with MockFactory with BeforeAndAfterAll {
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  trait Fixture {
    val ctx: ServerContextLike = mock[ServerContextLike]
    val api = new ApiImpl(ctx)
  }
  trait Session {
    _: Fixture =>
    val sessionKey = "foo"
    def sessionActorRef(pollRes: Seq[Event] = Seq.empty): ActorRef = system.actorOf(Props(new Actor {
      override def receive: Receive = {
        case "poll" => sender() ! pollRes
      }
    }))
  }

  "Echo API" should "return requested message" in new Fixture {
    val message = "foo"
    api.echo(message) shouldBe message
  }

  "Register API" should "add new player" in new Fixture {
    val name = "foo"

    (ctx.registerPlayer _).expects(name)

    val playerInfo: PlayerInfo = api.register(name)
    playerInfo.name shouldBe name
  }

  "Create Game API" should "create new Game" in new Fixture with Session {
    (ctx.findGameBySessionKey _).expects(sessionKey).returning(None)
    val Right(gameInfo) = api.createGame(GameSetting(sessionKey))
  }

  it should "be error when already exists game created same player" in new Fixture with Session {
    (ctx.findGameBySessionKey _).expects(sessionKey).returning(Some(GameInfo(sessionKey)))
    val Left(CreateGameBySamePlayer) = api.createGame(GameSetting(sessionKey))
  }

  "Exec API" should "execute command" in new Fixture with Session {
    (ctx.findSession _).expects(sessionKey).returning(Some(system.actorOf(TestActors.echoActorProps)))
    api.exec(Sync(sessionKey)) shouldBe Accepted
  }

  it should "reject command on invalid session" in new Fixture with Session {
    (ctx.findSession _).expects(sessionKey).returning(None)
    api.exec(Sync(sessionKey)) shouldBe Rejected
  }

  "Poll API" should "return NoEvent when player's queue is empty" in new Fixture with Session {
    (ctx.findSession _).expects(sessionKey).returning(None)
    Await.result(api.poll(sessionKey), Duration.Inf).size shouldBe 0
  }

  it should "return events on player's queue" in new Fixture with Session {
    (ctx.findSession _)
      .expects(sessionKey)
      .returning(Some(sessionActorRef(Seq(NoEvent, NoEvent, NoEvent))))
    Await.result(api.poll(sessionKey), Duration.Inf).size shouldBe 3
  }
}
