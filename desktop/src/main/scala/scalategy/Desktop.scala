package scalategy

import com.badlogic.gdx.backends.lwjgl.LwjglApplication

class Desktop {
  val width = 800
  val height = 600
  def initialize(): Unit = {
    new LwjglApplication(appListener, "scalategy", width, height)
  }
  def appListener: AppListener = new AppListener(width, height)
}
object Desktop extends Desktop with App {
  initialize()
}
