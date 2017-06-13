package scalategy

import scalategy.shared.settings.NetworkSettingsLike

trait AppSettings {
  lazy val baseUrl: String = networkSettings.baseUrl
  val stageWidth: Int
  val stageHeight: Int
  val networkSettings: NetworkSettingsLike
}
