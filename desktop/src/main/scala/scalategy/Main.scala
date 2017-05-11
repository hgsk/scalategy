package scalategy

import com.badlogic.gdx.backends.lwjgl.LwjglApplication

object Main extends App {
  val width = 800
  val height = 600
  new LwjglApplication(new AppListener(width, height), "scalategy", width, height)
}
