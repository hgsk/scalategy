package scalategy.shared.models

trait MovableEntity extends FieldEntity {
  val gameUnits: Set[GameUnit]
  def currentTile: Tile
  def moveTo(targetTile: Tile)(implicit env: EnvironmentLike): MovementLike =
    Movement(currentTile, targetTile, env.now, calcDuration(targetTile), this)
  def calcDuration(targetTile: Tile): Long = (currentTile.distance(targetTile) / calcSpeed).toLong
  def calcSpeed: Double = gameUnits.map(_.speed).min
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
  val entity: MovableEntity
}
case class Movement(
  from: Tile,
  to: Tile,
  startedAt: Long,
  duration: Long,
  entity: MovableEntity
) extends MovementLike
