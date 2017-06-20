package scalategy.shared.models

trait EnvironmentLike {
  def now: Long
}
case class Environment(now: Long) extends EnvironmentLike
