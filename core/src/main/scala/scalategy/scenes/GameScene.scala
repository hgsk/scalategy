package scalategy.scenes

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Label, Table}
import com.badlogic.gdx.utils.Align

import scalategy.components.MapView

class GameScene extends Scene {
  val ASSET_CIRCLE = "circle.png"
  val ASSET_SQUARE = "square.png"
  val ASSET_DIAMOND = "diamond.png"
  val ASSET_FONT = "caladea_bold.fnt"
  override def assets: Assets = {
    super.assets ++ Set(
      (ASSET_CIRCLE, classOf[Texture]),
      (ASSET_SQUARE, classOf[Texture]),
      (ASSET_DIAMOND, classOf[Texture]),
      (ASSET_FONT, classOf[BitmapFont])
    ) ++ MapView.assets
  }
  override def enter(assetManager: AssetManager): Group = {
    val main = new Group
    val bitmapFont = assetManager.get[BitmapFont](ASSET_FONT)
    val table = new Table()
    val labelStyle = new LabelStyle(bitmapFont, new Color(0xffffffff))
    main.addActor(MapView().initialize(assetManager))
    table.add(new Label("Header", labelStyle)).align(Align.left)
    table.row()
    table.add(main).expand().align(Align.bottomLeft)
    table.row()
    table.add(new Label("Footer", labelStyle)).align(Align.left)
    table.setFillParent(true)
    table
  }
  override def update(assetManager: AssetManager): Unit = ()
  override def exit(assetManager: AssetManager): Unit = ()
}
