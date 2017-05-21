package scalategy.shared.models

/**
  * マップ情報
  */
case class MapData(mapSize: MapSize, selectedTile: Set[Tile])
object MapData {
  def empty(sizeX: Int, sizeY: Int) = MapData(MapSize(sizeX, sizeY), Set.empty)
}

case class MapSize(x: Int, y: Int)
case class Tile(x: Int, y: Int)

