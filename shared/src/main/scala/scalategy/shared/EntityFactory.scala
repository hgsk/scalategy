package scalategy.shared

import scalategy.shared.models._

class EntityFactory extends EntityFactoryLike[Long] {
  override val uniqueIdGenerator: UniqueIdGenerator[Long] = new SequentialUniqueIdGenerator
  def movableEntity(gameUnits: Set[GameUnit], coordinates: Coordinates): MovableEntity = MovableEntity(nextId, gameUnits, coordinates)
  def fixedEntity(currentTile: Tile): FixedEntity = new FixedEntity {
    override def entityId: Long = nextId
    override val tile: Tile = currentTile
  }
}
trait EntityFactoryLike[ID] {
  protected val uniqueIdGenerator: UniqueIdGenerator[ID]
  def nextId: ID = uniqueIdGenerator.nextId
}

trait UniqueIdGenerator[ID] {
  def nextId: ID
}
class SequentialUniqueIdGenerator(initialValue: Long = 0) extends UniqueIdGenerator[Long] {
  private var sequence: Long = initialValue
  override def nextId: Long = {
    sequence = sequence + 1
    sequence
  }
}
