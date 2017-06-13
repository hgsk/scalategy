package scalategy

import com.badlogic.gdx.Net.HttpMethods
import com.badlogic.gdx.net.HttpRequestBuilder

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scalategy.net.AutowireClient
import scalategy.shared.settings.{NetworkSettings, NetworkSettingsLike}
import scalategy.shared.{Api, Constants}

object Dev extends Desktop with App {
  implicit override val appSettings = new DevSettings {}
  override def appListener = new DevListener
  initialize()
}

class DevListener()(implicit appSettings: AppSettings) extends AppListener {
  import autowire._

  private val apiClient = new AutowireClient(session)[Api]

  override def create(): Unit = {
    super.create()
    startSessionTest()
  }
  def startSessionTest(): Unit = {
    val builder = new HttpRequestBuilder
    builder
      .newRequest()
      .method(HttpMethods.POST)
      .url(s"${appSettings.baseUrl}/session")
    gdxs.net.sendHttpRequest(builder.build())
      .onSuccess {
        case res =>
          session.token = res.getHeader(Constants.sessionHeaderName)
          echoTest("Hello libGDX!")
      }
  }
  def echoTest(message: String): Unit = {
    apiClient
      .echo(message)
      .call()
      .onComplete {
        case Success(r) => println(r)
        case Failure(t) =>
          println(t.getMessage)
          println(t.getStackTrace.mkString("\n"))
      }
  }
}

trait DevSettings extends DesktopSettings {
  override lazy val networkSettings: NetworkSettingsLike = NetworkSettings("localhost", 9000)
}
