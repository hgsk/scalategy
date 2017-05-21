package scalategy.scenes

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Image, Label}

import scalategy.AppSettings
import scalategy.components.MapView

class GameScene()(implicit val appSettings: AppSettings) extends Scene {
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
    val squareTexture = assetManager.get[Texture](ASSET_SQUARE)
    val labelStyle = new LabelStyle(bitmapFont, new Color(0xffffffff))
    val headerBg = new Image(squareTexture)
    val footerBg = new Image(squareTexture)
    val headerLabel = new Label("Header", labelStyle)
    headerBg.setBounds(0, 570, 800, 30)
    headerBg.setColor(0, 0, 0, 1)
    headerLabel.setPosition(10, 570)
    main.addActor(MapView().initialize(assetManager))
    main.addActor(headerBg)
    main.addActor(headerLabel)

    footerBg.setBounds(0, 0, 800, 30)
    footerBg.setColor(0, 0, 0, 1)
    main.addActor(footerBg)
    main.addActor(new Label("Footer", labelStyle))
    main
  }
  override def update(assetManager: AssetManager): Unit = ()
  override def exit(assetManager: AssetManager): Unit = ()
}
