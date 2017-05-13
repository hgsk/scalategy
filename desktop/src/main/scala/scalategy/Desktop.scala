package scalategy

import com.badlogic.gdx.backends.lwjgl.LwjglApplication

class Desktop {
  val width = 800
  val height = 600
  new LwjglApplication(new AppListener(width, height), "scalategy", width, height)
}
object Desktop extends Desktop with App
