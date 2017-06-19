package scalategy.shared.models

trait GameUnit {
  val speed: Int
}
object GameUnit {
  def apply(initialSpeed: Int): GameUnit = new GameUnit {
    val speed: Int = initialSpeed
  }
}
