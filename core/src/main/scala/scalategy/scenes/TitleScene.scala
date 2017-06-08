package scalategy.scenes

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Table, TextButton}
import com.badlogic.gdx.scenes.scene2d.utils.{ClickListener, NinePatchDrawable}
import com.badlogic.gdx.scenes.scene2d.{Group, InputEvent}
import gdxs.graphics.g2d.NinePatch
import gdxs.scenes.scene2d.ui.Button

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
    import gdxs.Implicits._
    val group = new Table
    group.setFillParent(true)
    group.add(
      Button
        .text("Start", assetManager.get(ASSET_FONT))
        .up(assetManager.nine(ASSET_NINE).build(8))
        .down(assetManager.nine(ASSET_NINE_DOWN).build(8))
        .over(assetManager.nine(ASSET_NINE_OVER).build(8))
        .clicked(() => startButtonClicked = true)
        .build()
    )
    group
  }
  override def update(assetManager: AssetManager): Option[Scene] = if (startButtonClicked) Some(new GameScene) else None
  override def exit(assetManager: AssetManager): Unit = ()
}
