package scalategy.shared.settings

case class NetworkSettings(host: String, port: Int) extends NetworkSettingsLike
object NetworkSettings {
  def empty = new NetworkSettings("", 0) {
    override lazy val baseUrl: String = sys.error("ネットワーク設定が未定義")
  }
}

trait NetworkSettingsLike {
  lazy val baseUrl: String = s"http://$host:$port"
  val host: String
  val port: Int
}
