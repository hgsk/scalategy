package scalategy.shared.models

trait MapDataLike {
  val mapSize: MapSize
  val entityMap: Map[Long, FieldEntity]
}

case class MapSize(x: Int, y: Int)
case class Tile(x: Int, y: Int) {
  import scalategy.shared.Constants.{tileBorder, tileSize}
  def distance(that: Tile): Double = Math.sqrt(Math.pow(that.x - x, 2) + Math.pow(that.y - y, 2))
  def toCoordinates: Coordinates = Coordinates(x * (tileSize + tileBorder), y * (tileSize + tileBorder))
}

