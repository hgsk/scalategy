package scalategy.shared.models

case class Coordinates(x: Int, y: Int) extends Vector2D {
  import scalategy.shared.Constants.{tileSize, tileBorder}
  def toTile: Tile = Tile(x / (tileSize + tileBorder), y / (tileSize + tileBorder))
  def modX(f: Int => Int): Coordinates = copy(x = f(x))
  def modY(f: Int => Int): Coordinates = copy(x = f(x))
  def modXY(f: Int => Int): Coordinates = copy(x = f(x), y = f(y))
  def add(v: Vector2D): Coordinates = Coordinates(x + v.x, y + v.y)
}
trait Vector2D {
  val x: Int
  val y: Int
  def -(that: Vector2D): Vector2D = Vector2D(x - that.x, y - that.y)
  def *(d: Double): Vector2D = Vector2D((x * d).toInt, (y * d).toInt)
}
object Vector2D {
  def apply(vx: Int, vy: Int): Vector2D = new Vector2D {
    val x: Int = vx
    val y: Int = vy
  }
}
