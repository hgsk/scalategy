package scalategy

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.{GL20, Texture}
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.{ApplicationListener, Gdx}

class AppListener(stageWidth: Int, stageHeight: Int) extends ApplicationListener {
  lazy val assetManager = new AssetManager()
  lazy val stage: Stage = new Stage(new FitViewport(stageWidth, stageHeight))
  private var initialized: Boolean = false
  def initialize(): Unit = {
    val circleTexture = assetManager.get[Texture]("circle.png")
    for (_ <- 1 to 100) {
      val circleImage = new Image(circleTexture)
      circleImage.setColor(Math.random().toFloat, Math.random().toFloat, Math.random().toFloat, 1)
      circleImage.setSize(10, 10)
      circleImage.setPosition((Math.random() * stageWidth).toFloat, (Math.random() * stageHeight).toFloat)
      stage.addActor(circleImage)
    }
    initialized = true
  }
  override def create(): Unit = {
    Gdx.input.setInputProcessor(stage)
    assetManager.load("circle.png", classOf[Texture])
  }
  override def render(): Unit = {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    Gdx.gl.glClearColor(0, 0, 0, 1)

    if (assetManager.update()) {
      if (!initialized) {
        initialize()
      }
    } else {
      // Now Loading...
    }

    stage.act(Gdx.graphics.getDeltaTime)
    stage.draw()

  }
  override def dispose(): Unit = {
    assetManager.dispose()
    stage.dispose()
  }
  override def resize(width: Int, height: Int): Unit = {
    stage.getViewport.update(width, height)
  }
  override def resume(): Unit = ()
  override def pause(): Unit = ()
}
