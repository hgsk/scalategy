package scalategy.scenes

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{BitmapFont, NinePatch}
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.{ClickListener, NinePatchDrawable}
import com.badlogic.gdx.scenes.scene2d.{Group, InputEvent}

import scalategy.AppSettings

class TitleScene()(implicit appSettings: AppSettings) extends Scene {
  scene =>
  val ASSET_FONT = "caladea_bold.fnt"
  val ASSET_NINE = "9.png"
  val ASSET_NINE_DOWN = "9_down.png"
  val ASSET_NINE_OVER = "9_over.png"
  private var startButtonClicked = false
  override def assets: Assets = {
    super.assets ++ Set(
      (ASSET_FONT, classOf[BitmapFont]),
      (ASSET_NINE, classOf[Texture]),
      (ASSET_NINE_DOWN, classOf[Texture]),
      (ASSET_NINE_OVER, classOf[Texture])
    )
  }
  override def enter(assetManager: AssetManager): Group = {
    val group = new Group
    val nine = new NinePatchDrawable(new NinePatch(assetManager.get[Texture](ASSET_NINE), 8, 8, 8, 8))
    val nineDown = new NinePatchDrawable(new NinePatch(assetManager.get[Texture](ASSET_NINE_DOWN), 8, 8, 8, 8))
    val nineOver = new NinePatchDrawable(new NinePatch(assetManager.get[Texture](ASSET_NINE_OVER), 8, 8, 8, 8))
    val textButtonStyle = new TextButtonStyle(nine, nineDown, null, assetManager.get(ASSET_FONT))
    textButtonStyle.over = nineOver
    val startButton = new TextButton("Start", textButtonStyle)
    startButton.addListener(new ClickListener {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {
        startButtonClicked = true
      }
    })
    group.addActor(startButton)
    group
  }
  override def update(assetManager: AssetManager): Option[Scene] = if (startButtonClicked) Some(new GameScene) else None
  override def exit(assetManager: AssetManager): Unit = ()
}
