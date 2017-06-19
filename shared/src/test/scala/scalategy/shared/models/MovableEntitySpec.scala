package scalategy.shared.models

import org.scalatest.{FlatSpec, Matchers}

class MovableEntitySpec extends FlatSpec with Matchers {
  trait Fixture {
    implicit val env: EnvironmentLike = new EnvironmentLike {
      override def now: Long = 0
    }
    val fromTile = Tile(10, 10)
    val toTile = Tile(15, 15)
    def entity: MovableEntityLike = new MovableEntityLike {
      override val gameUnits: Set[GameUnit] = Set(gameUnit)
      override def coordinates: Coordinates = fromTile.toCoordinates
    }
    def entity(entityGameUnits: Set[GameUnit]): MovableEntityLike = new MovableEntityLike {
      override val gameUnits: Set[GameUnit] = entityGameUnits
      override def coordinates: Coordinates = fromTile.toCoordinates
    }
    def gameUnit: GameUnit = gameUnit(1)
    def gameUnit(gameUnitSpeed: Int): GameUnit = new GameUnit {
      override val speed: Int = gameUnitSpeed
    }
  }

  behavior of "A MovableEntity"

  it should "calculate speed" in new Fixture {
    entity.calcSpeed shouldBe 1
    entity(Set(gameUnit(2))).calcSpeed shouldBe 2
    entity(Set(gameUnit(2), gameUnit(3), gameUnit(4))).calcSpeed shouldBe 2
  }

  it should "calculate duration" in new Fixture {
    entity.calcDuration(fromTile) shouldBe 0
    entity.calcDuration(toTile) shouldBe 7
  }

  it should "be movable" in new Fixture {
    val movement: MovementLike = entity.moveTo(toTile)
    movement.duration shouldBe 7
  }
}
