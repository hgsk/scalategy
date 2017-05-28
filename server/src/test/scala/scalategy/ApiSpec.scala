package scalategy

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scalategy.shared._

class ApiSpec extends FlatSpec with Matchers with MockFactory {
  trait Fixture {
    val ctx: ServerContextLike = mock[ServerContextLike]
    val api = new ApiImpl(ctx)
  }
  trait Session {
    _: Fixture =>
    val sessionKey = "foo"
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
}
