package scalategy.shared.models

trait MapDataLike {
  val mapSize: MapSize
  val entityMap: Map[Tile, FieldEntity]
}

case class MapSize(x: Int, y: Int)
case class Tile(x: Int, y: Int)

