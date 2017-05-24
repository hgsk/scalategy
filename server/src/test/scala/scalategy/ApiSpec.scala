package scalategy

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class ApiSpec extends FlatSpec with Matchers with MockFactory {
  val api = new ApiImpl

  "Echo API" should "return requested message" in {
    val message = "foo"
    api.echo(message) shouldBe message
  }

  "Register API" should "add new player" in {
    val name = "foo"
    val playerInfo = api.register(name)
    playerInfo.name shouldBe name
  }
}
