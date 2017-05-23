package scalategy.shared

trait Api {
  def echo(message: String): String
  def register(name: String): PlayerInfo
  def createGame(gameSetting: GameSetting): GameInfo
  def exec(command: Command): ExecStatus
  def poll(): Event
}

sealed trait Command
sealed trait Event
case object NoEvent extends Event
sealed trait ExecStatus
case object Accepted extends ExecStatus

case class GameSetting()
case class GameInfo()
case class PlayerInfo()

