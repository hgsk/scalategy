package scalategy.components

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.{Actor, Group}
import com.badlogic.gdx.scenes.scene2d.ui.{Image, Table}
import com.badlogic.gdx.utils.Align

import scalategy.common.UseAssets

case class MapView() extends Group with Component {
  import MapView._
  override def initialize(assetManager: AssetManager): Actor = {
    val table = new Table()
    val squareTexture = assetManager.get[Texture](ASSET_SQUARE)
    for (x <- 0 to 9; y <- 0 to 9) {
      val square = new Image(squareTexture)
      square.setPosition(x * 101, y * 101)
      addActor(square)
    }
    table.setClip(true)
    table.setBounds(0, 0, 800, 520)
    table.add(this).expand().align(Align.bottomLeft)
    table
  }
}
object MapView extends UseAssets {
  val ASSET_SQUARE = "square.png"
  override def assets: Assets = Set(
    (ASSET_SQUARE, classOf[Texture])
  )
}
