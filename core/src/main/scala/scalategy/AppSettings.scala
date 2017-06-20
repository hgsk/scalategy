package scalategy

import scalategy.shared.settings.NetworkSettingsLike

trait AppSettings {
  lazy val baseUrl: String = networkSettings.baseUrl
  val stageWidth: Int
  val stageHeight: Int
  val networkSettings: NetworkSettingsLike
  val mapSettings: MapSettings
}

trait MapSettings {
  val offsetX: Int
  val offsetY: Int
  val mapWidth: Int
  val mapHeight: Int
  val dragSensitivity: Int
}
