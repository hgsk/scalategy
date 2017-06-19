package scalategy.shared.utils

trait ClockLike {
  def now(): Long
}

class ManualClock(currentTime: Long = 0) extends ClockLike {
  override def now(): Long = currentTime
  def past(time: Long): ManualClock = new ManualClock(currentTime + time)
}
