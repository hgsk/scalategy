package scalategy.shared.models

trait MapDataLike {
  val mapSize: MapSize
  val entityMap: Map[Tile, FieldEntity]
}

case class MapSize(x: Int, y: Int)
case class Tile(x: Int, y: Int) {
  def distance(that: Tile): Double = Math.sqrt(Math.pow(that.x - x, 2) + Math.pow(that.y - y, 2))
}

