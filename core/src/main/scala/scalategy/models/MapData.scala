package scalategy.models

import scalategy.shared.models.{FieldEntity, MapDataLike, MapSize, Tile}

case class MapData(mapSize: MapSize, selectedTiles: Set[Tile], entityMap: Map[Long, FieldEntity]) extends MapDataLike
object MapData {
  def empty(mapSize: MapSize) = MapData(mapSize, Set.empty, Map.empty)
}

