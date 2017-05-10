package scalategy

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.{Game, Gdx}

class AppListener extends Game {
  lazy val stage: Stage = new Stage(new FitViewport(800, 600))
  override def create(): Unit = {
    Gdx.input.setInputProcessor(stage)
  }
  override def render(): Unit = {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    Gdx.gl.glClearColor(0, 0, 0, 1)

    stage.act(Gdx.graphics.getDeltaTime)
    stage.draw()
  }
  override def dispose(): Unit = {
    stage.dispose()
  }
  override def resize(width: Int, height: Int): Unit = {
    stage.getViewport.update(width, height)
  }
}
