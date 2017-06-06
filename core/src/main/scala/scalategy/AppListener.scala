package scalategy

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.{ApplicationListener, Gdx}

import scalategy.scenes.{Scene, TitleScene}

class AppListener()(implicit appSettings: AppSettings) extends ApplicationListener {
  import appSettings._

  // FIXME 暫定でここに設置
  protected val session = new Session

  lazy val assetManager = new AssetManager()
  lazy val stage: Stage = new Stage(new FitViewport(stageWidth, stageHeight))
  private var initialized: Boolean = false
  private var currentScene: Option[Scene] = None
  def changeScene(scene: Scene): Unit = {
    currentScene.map(_.assets).foreach(_.foreach { case (file, _) =>
      assetManager.unload(file)
    })
    currentScene.foreach(_.exit(assetManager))
    stage.clear()
    currentScene = Some(scene)
    initialized = false
    scene.assets.foreach { case (file, clazz) =>
      assetManager.load(file, clazz)
    }
  }
  override def create(): Unit = {
    Gdx.input.setInputProcessor(stage)
    changeScene(new TitleScene)
  }
  override def render(): Unit = {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    Gdx.gl.glClearColor(0, 0, 0, 1)

    if (!initialized) {
      if (assetManager.update()) {
        currentScene.foreach(scene => stage.addActor(scene.enter(assetManager)))
        initialized = true
      } else {
        // Now Loading...
      }
    } else {
      currentScene
        .flatMap(_.update(assetManager))
        .foreach(changeScene)
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

// FIXME 暫定でここに設置
class Session {
  var token: Option[String] = None
}

object CanceledException extends Throwable
