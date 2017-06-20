package scalategy

import com.badlogic.gdx.Net.HttpMethods
import com.badlogic.gdx.net.HttpRequestBuilder
import com.badlogic.gdx.scenes.scene2d.Group
import gdxs.Implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scalategy.components.MapView.AddEntities
import scalategy.net.AutowireClient
import scalategy.scenes.GameScene.{GameCircuit, MoveTo}
import scalategy.scenes.{GameScene, Scene}
import scalategy.shared.models.{GameUnit, Tile}
import scalategy.shared.settings.{NetworkSettings, NetworkSettingsLike}
import scalategy.shared.{Api, Constants, EntityFactory}

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
  override def entered(sceneAndGroup: (Scene, Group)): Group = sceneAndGroup match {
    case (gameScene: GameScene, group) =>
      addEntitiesTest(gameScene.circuit, gameScene.entityFactory)
      group
    case (_, group) => group
  }
  def addEntitiesTest(circuit: GameCircuit, entityFactory: EntityFactory): Unit = {
    val me = entityFactory.movableEntity(Set(GameUnit(10)), Tile(1, 3).toCoordinates)
    val fe = entityFactory.fixedEntity(Tile(1, 1))
    circuit(AddEntities(Seq(fe, me)))
    circuit(MoveTo(me, Tile(10, 10)))
  }
  def startSessionTest(): Unit = {
    val builder = new HttpRequestBuilder
    builder
      .newRequest()
      .method(HttpMethods.POST)
      .url(s"${appSettings.baseUrl}/session")
      .send()
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
