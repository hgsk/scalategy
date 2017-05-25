package scalategy

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class ApiSpec extends FlatSpec with Matchers with MockFactory {
  trait Fixture {
    val ctx = mock[ServerContextLike]
    val api = new ApiImpl(ctx)
  }

  "Echo API" should "return requested message" in new Fixture {
    val message = "foo"
    api.echo(message) shouldBe message
  }

  "Register API" should "add new player" in new Fixture {
    val name = "foo"

    (ctx.registerPlayer _).expects(name)

    val playerInfo = api.register(name)
    playerInfo.name shouldBe name
  }

  "Create Game API" should "create new Game" in new Fixture {
  }
}
