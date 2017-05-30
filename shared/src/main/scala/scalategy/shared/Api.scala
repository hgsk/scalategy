package scalategy.shared

import scala.concurrent.Future

trait Api {
  def echo(message: String): String
  def register(name: String): PlayerInfo
  def createGame(gameSetting: GameSetting): Either[ServerError, GameInfo]
  def exec(command: Command): ExecStatus
  def poll(sessionKey: String): Future[Seq[Event]]
}

sealed trait ServerError
case object UnknownServerError extends ServerError
case object CreateGameBySamePlayer extends ServerError

sealed trait Command {
  val sessionKey: String
}
final case class Sync(sessionKey: String) extends Command

sealed trait Event
case object NoEvent extends Event

sealed trait ExecStatus
case object Accepted extends ExecStatus
case object Rejected extends ExecStatus

case class GameSetting(sessionKey: String)
case class GameInfo(sessionKey: String)
case class PlayerInfo(name: String)

