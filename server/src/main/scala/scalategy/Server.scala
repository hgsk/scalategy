package scalategy

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import upickle.default._

import scala.util.Try
import scalategy.core.ApiImpl
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
  protected var players: Map[String, Player] = Map.empty
  protected var sessions: Map[String, ActorRef] = Map.empty
  def registerPlayer(name: String): Try[Player] = Try(Player())
  def findSession(sessionKey: String): Option[ActorRef] = sessions.get(sessionKey)
  def findGameBySessionKey(sessionKey: String): Option[GameInfo] = gameInfos.find(_.sessionKey == sessionKey)
}
object ServerContext extends ServerContextLike
object AutowireServer extends autowire.Server[String, Reader, Writer] {
  override def read[R: Reader](p: String): R = upickle.default.read[R](p)
  override def write[R: Writer](r: R): String = upickle.default.write(r)
}
