package scalategy.shared.models

import org.scalatest.{FlatSpec, Matchers}

import scalategy.shared.models.ResourceTypes._

class StockSpec extends FlatSpec with Matchers {

  behavior of "A stock"

  it should "keep added resource" in {
    val stock = Stock()
    ResourceTypes.map(stock.amount).foreach(_.shouldBe(0))
    stock.add(Resource.food(20)).amount(Food) shouldBe 20
    stock.add(Resource.wood(10)).amount(Wood) shouldBe 10
    stock.add(Resource.soil(5)).amount(Soil) shouldBe 5
    stock.add(Resource.water(3)).amount(Water) shouldBe 3
    stock.add(Resource.gold(2)).amount(Gold) shouldBe 2
    stock.add(Wood, 100).amount(Wood) shouldBe 100
  }

  it should "cut down resource over capacity" in {
    val stock = Stock().updateCapacity(Wood, 100)
    stock.add(Wood, 200).amount(Wood) shouldBe 100
    stock.add(Wood, 100).updateCapacity(Wood, 50).amount(Wood) shouldBe 50
    Stock(0, 100).add(Gold, 200).amount(Gold) shouldBe 100
  }

  it should "reduce resource if consumed" in {
    val stock = Stock(100)
    stock.consume(Resource.wood(50)).foreach(_.amount(Wood) shouldBe 50)
    stock.consume(Food, 20).foreach(_.amount(Food) shouldBe 80)
    stock.consume(Water, 100).foreach(_.amount(Water) shouldBe 0)
    stock.consume(Water, 100).isSuccess shouldBe true
    stock.consume(Gold, 101).isFailure shouldBe true
  }
}
