package scalategy.shared.models

case class MovableEntity(entityId: Long, gameUnits: Set[GameUnit], coordinates: Coordinates) extends MovableEntityLike
trait MovableEntityLike extends FieldEntity {
  val speedCoefficient = 0.001d
  val gameUnits: Set[GameUnit]
  def currentTile: Tile = coordinates.toTile
  def moveTo(targetTile: Tile)(implicit env: EnvironmentLike): Movement =
    Movement(currentTile, targetTile, env.now, calcDuration(targetTile), this)
  def calcDuration(targetTile: Tile): Long = (currentTile.distance(targetTile) / calcSpeed).toLong
  def calcSpeed: Double = gameUnits.map(_.speed).min * speedCoefficient
}

object MovableEntityTypes {
  sealed trait MovableEntityType
  case object Gathering extends MovableEntityType
  case object Attack extends MovableEntityType
  case object Building extends MovableEntityType
}

trait MovementLike {
  val from: Tile
  val to: Tile
  val startedAt: Long
  val duration: Long
  val entity: MovableEntityLike
  lazy val finishedAt: Long = startedAt + duration
  def coordinates(now: Long): Coordinates =
    from
      .toCoordinates
      .add((to.toCoordinates - from.toCoordinates) * ((now - startedAt).toDouble / duration.max(1)))
}
case class Movement(
  from: Tile,
  to: Tile,
  startedAt: Long,
  duration: Long,
  entity: MovableEntityLike
) extends MovementLike
