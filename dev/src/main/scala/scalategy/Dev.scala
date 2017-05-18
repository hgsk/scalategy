package scalategy

import scala.util.{Failure, Success}
import scalategy.shared.Api

object Dev extends Desktop with App {
  implicit override val appSettings = new DevSettings {}
  override def appListener = new DevListener
  initialize()
}

class DevListener()(implicit appSettings: AppSettings) extends AppListener {
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

trait DevSettings extends DesktopSettings