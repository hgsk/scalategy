package scalategy.shared.models

import org.scalatest.{FlatSpec, Matchers}

class TileSpec extends FlatSpec with Matchers {

  behavior of "A Tile"

  it should "calculate distance to other tile" in {
    val zero = Tile(0, 0)
    val one = Tile(1, 1)
    zero.distance(zero) shouldBe 0
    zero.distance(Tile(1, 0)) shouldBe 1
    zero.distance(one) shouldBe Math.sqrt(2)
    zero.distance(Tile(2, 2)) shouldBe Math.sqrt(8)
    one.distance(one) shouldBe 0
    one.distance(zero) shouldBe Math.sqrt(2)
    one.distance(Tile(1, 0)) shouldBe 1
  }
}
