package scalategy.components

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.{Actor, Group, InputEvent, InputListener}

import scalategy.AppSettings
import scalategy.common.UseAssets

case class MapView()(implicit appSettings: AppSettings) extends Group with Component {
  import MapView._
  import appSettings.stageHeight
  val xOffset = 0
  val yOffset = 30
  override def initialize(assetManager: AssetManager): Actor = {
    val squareTexture = assetManager.get[Texture](ASSET_SQUARE)
    val mapSize = 20
    val mapWidth = 800
    val mapHeight = 540
    val tileSize = 40
    val minX = ((tileSize + 1) * mapSize - mapWidth - xOffset) * -1
    val minY = ((tileSize + 1) * mapSize - mapHeight - yOffset) * -1
    setPosition(xOffset, yOffset)
    for (x <- 0 until mapSize; y <- 0 until mapSize) {
      val square = new Image(squareTexture)
      square.setBounds(x * (tileSize + 1), y * (tileSize + 1), tileSize, tileSize)
      addActor(square)
    }
    addListener(new InputListener() {
      private var pos: (Float, Float) = _
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean = {
        pos = (Gdx.input.getX - getX, stageHeight - Gdx.input.getY - getY)
        true
      }
      override def touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int): Unit = {
        setPosition(
          (Gdx.input.getX - pos._1).max(minX).min(xOffset),
          (stageHeight - Gdx.input.getY - pos._2).max(minY).min(yOffset)
        )
      }
    })
    this
  }
  override def draw(batch: Batch, parentAlpha: Float): Unit = {
    super.draw(batch, parentAlpha)
  }

}
object MapView extends UseAssets {
  val ASSET_SQUARE = "square.png"
  override def assets: Assets = Set(
    (ASSET_SQUARE, classOf[Texture])
  )
}
