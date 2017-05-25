package scalategy

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import upickle.default._

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
  def registerPlayer(name: String): Try[Player]
}
object ServerContext extends ServerContextLike {
  override def registerPlayer(name: String): Try[Player] = ???
}

class ApiImpl(context: ServerContextLike) extends Api {
  override def echo(message: String): String = message
  override def register(name: String): PlayerInfo = {
    context.registerPlayer(name)
    PlayerInfo(name)
  }
  override def createGame(gameSetting: GameSetting): GameInfo = ???
  override def exec(command: Command): ExecStatus = ???
  override def poll(): Event = ???
}

object AutowireServer extends autowire.Server[String, Reader, Writer] {
  override def read[R: Reader](p: String): R = upickle.default.read[R](p)
  override def write[R: Writer](r: R): String = upickle.default.write(r)
}

