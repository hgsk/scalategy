package scalategy

import scala.util.{Failure, Success}
import scalategy.shared.Api

object Dev extends Desktop with App {
  override def appListener = new DevListener(width, height)
  initialize()
}

class DevListener(width: Int, height: Int) extends AppListener(width, height) {
  import autowire._

  import scala.concurrent.ExecutionContext.Implicits.global
  override def create(): Unit = {
    super.create()
    AutowireClient[Api]
      .echo("Hello libGDX!")
      .call()
      .onComplete {
        case Success(r) => println(r)
        case Failure(t) => println(t.getStackTrace.mkString("\n"))
      }
  }
}