package scalategy

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import upickle.default._

import scala.concurrent.Future
import scala.util.Try
import scalategy.models.Player
import scalategy.shared._

object Server extends App {
  implicit val system = ActorSystem("server")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val route =
    (post & path("api" / Segments)) { segments =>
      entity(as[String]) { s =>
        complete {
          AutowireServer.route[Api](new ApiImpl(ServerContext)) {
            autowire.Core.Request(
              segments,
              upickle.default.read[Map[String, String]](s)
            )
          }
        }
      }
    }

  Http().bindAndHandle(route, "0.0.0.0", 9000)
}

trait ServerContextLike {
  protected var gameInfos: Seq[GameInfo] = Seq.empty
  def registerPlayer(name: String): Try[Player]
  def findSession(sessionKey: String): Option[ActorRef] = ???
  def findGameBySessionKey(sessionKey: String): Option[GameInfo] = gameInfos.find(_.sessionKey == sessionKey)
}
object ServerContext extends ServerContextLike {
  override def registerPlayer(name: String): Try[Player] = ???
}

class ApiImpl(context: ServerContextLike)(implicit system: ActorSystem) extends Api {
  import system.dispatcher

  override def echo(message: String): String = message
  override def register(name: String): PlayerInfo = {
    context.registerPlayer(name)
    PlayerInfo(name)
  }
  override def createGame(gameSetting: GameSetting): Either[ServerError, GameInfo] =
    if (context.findGameBySessionKey(gameSetting.sessionKey).isDefined) Left(CreateGameBySamePlayer)
    else Right(GameInfo(gameSetting.sessionKey))
  override def exec(command: Command): ExecStatus = {
    context.findSession(command.sessionKey) match {
      case Some(_) => Accepted
      case None => Rejected
    }
  }
  override def poll(): Future[Event] = Future(NoEvent)
}

object AutowireServer extends autowire.Server[String, Reader, Writer] {
  override def read[R: Reader](p: String): R = upickle.default.read[R](p)
  override def write[R: Writer](r: R): String = upickle.default.write(r)
}

