package scalategy

import com.badlogic.gdx.backends.lwjgl.LwjglApplication

import scalategy.shared.settings.{NetworkSettings, NetworkSettingsLike}

class Desktop {
  implicit val appSettings = new DesktopSettings {}
  def initialize(): Unit = {
    new LwjglApplication(appListener, "scalategy", appSettings.stageWidth, appSettings.stageHeight)
  }
  def appListener: AppListener = new AppListener
}
object Desktop extends Desktop with App {
  initialize()
}
trait DesktopSettings extends AppSettings {
  override lazy val networkSettings: NetworkSettingsLike = NetworkSettings.empty
  override val stageWidth: Int = 800
  override val stageHeight: Int = 600
  override val mapSettings = new MapSettings {
    override val offsetX: Int = 0
    override val offsetY: Int = 30
    override val mapWidth: Int = stageWidth
    override val mapHeight: Int = stageHeight - offsetY * 2
    override val dragSensitivity: Int = 5 * 5 * 2
  }
}
