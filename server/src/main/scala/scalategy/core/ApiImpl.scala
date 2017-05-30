package scalategy.core

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import scalategy.ServerContextLike
import scalategy.shared._

class ApiImpl(context: ServerContextLike)(implicit system: ActorSystem) extends Api {
  import system.dispatcher
  implicit val timeout = Timeout(3.second)

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
  override def poll(sessionKey: String): Future[Seq[Event]] = {
    context
      .findSession(sessionKey)
      .map { session =>
        session
          .ask("poll")
          .map(_.asInstanceOf[Seq[Event]])
      }
      .getOrElse(Future(Seq.empty))
  }
}
