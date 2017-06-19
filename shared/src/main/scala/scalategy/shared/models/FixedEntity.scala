package scalategy.shared.models

trait FixedEntity extends FieldEntity {
  val tile: Tile
  override lazy val coordinates: Coordinates = tile.toCoordinates
}
