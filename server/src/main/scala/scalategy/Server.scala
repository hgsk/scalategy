package scalategy

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import com.softwaremill.session.{SessionConfig, SessionManager}
import upickle.default._

import scala.util.Try
import scalategy.core.ApiImpl
import scalategy.models.Player
import scalategy.shared._

object Server extends App {
  implicit val system = ActorSystem("server")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val sessionConfig = SessionConfig.fromConfig()
  implicit val sessionManager = new SessionManager[String](sessionConfig)

  val route =
    pathSingleSlash {
      reject()
    } ~
      (post & path("session")) {
        val playerSession = ServerContext.registerSession
        setSession(oneOff, usingHeaders, playerSession.path.name) {
          complete("")
        }
      } ~
      (post & path("api" / Segments)) { segments =>
        requiredSession(oneOff, usingHeaders) { session =>
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
      }

  Http().bindAndHandle(route, "0.0.0.0", 9000)
}

class PlayerSession extends Actor {
  override def receive: Receive = {
    case _ =>
  }
}
object PlayerSession {
  def props() = Props(classOf[PlayerSession])
}

trait ServerContextLike {
  protected var gameInfos: Seq[GameInfo] = Seq.empty
  protected var players: Map[String, Player] = Map.empty
  protected var sessions: Map[String, ActorRef] = Map.empty
  def registerSession()(implicit system: ActorSystem): ActorRef = system.actorOf(PlayerSession.props())
  def registerPlayer(name: String): Try[Player] = Try(Player())
  def findSession(sessionKey: String): Option[ActorRef] = sessions.get(sessionKey)
  def findGameBySessionKey(sessionKey: String): Option[GameInfo] = gameInfos.find(_.sessionKey == sessionKey)
}
object ServerContext extends ServerContextLike
object AutowireServer extends autowire.Server[String, Reader, Writer] {
  override def read[R: Reader](p: String): R = upickle.default.read[R](p)
  override def write[R: Writer](r: R): String = upickle.default.write(r)
}
