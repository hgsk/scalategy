package scalategy.components

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.{Actor, InputEvent}
import diode.{Action, Dispatcher}
import gdxs.scenes.scene2d.Group
import gdxs.scenes.scene2d.ui.Image

import scalategy.AppSettings
import scalategy.common.UseAssets
import scalategy.models.MapData
import scalategy.shared.models.{FieldEntity, Tile}

case class MapView(initialMapData: MapData, dispatcher: Dispatcher)(implicit appSettings: AppSettings) extends Group with Component {
  import appSettings.stageHeight

  import MapView._
  val xOffset = 0
  val yOffset = 30
  val tileSize = 40
  val dragSensitivity: Int = 5 * 5 * 2
  val entityLayer = Group()
  private var tiles: Map[Tile, Actor] = Map.empty
  private var mapData: MapData = initialMapData
  def updateSelectedTiles(selectedTiles: Set[Tile]): Unit = {
    mapData.selectedTiles.map(tiles).foreach(_.setColor(1, 1, 1, 1))
    selectedTiles.map(tiles).foreach(_.setColor(1, 0, 0, 1))
    mapData = mapData.copy(selectedTiles = selectedTiles)
  }
  def updateEntityMap(entityMap: Map[Tile, FieldEntity], assetManager: AssetManager): Unit = {
    entityLayer.clear()
    val diamondTexture = assetManager.get[Texture](ASSET_DIAMOND)
    val yellow = new Color(.9f, .9f, 0, 1)
    for ((tile, _) <- entityMap) {
      val resource = new Image(diamondTexture)
      resource.setBounds(tile.x * (tileSize + 1) + tileSize / 4, tile.y * (tileSize + 1), tileSize / 2, tileSize)
      resource.setColor(yellow)
      entityLayer.addActor(resource)
    }
  }
  override def initialize(assetManager: AssetManager): Actor = {
    val squareTexture = assetManager.get[Texture](ASSET_SQUARE)
    val mapSizeX = mapData.mapSize.x
    val mapSizeY = mapData.mapSize.y
    val mapWidth = 800
    val mapHeight = 540
    val minX = ((tileSize + 1) * mapSizeX - mapWidth - xOffset) * -1
    val minY = ((tileSize + 1) * mapSizeY - mapHeight - yOffset) * -1
    setPosition(xOffset, yOffset)
    for (x <- 0 until mapSizeX; y <- 0 until mapSizeY) {
      val square = Image(squareTexture)
      square.setBounds(x * (tileSize + 1), y * (tileSize + 1), tileSize, tileSize)
      addActor(square)
      tiles = tiles.updated(Tile(x, y), square)
    }
    addListener(new ClickListener() {
      private var pos: (Float, Float) = _
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean = {
        pos = (Gdx.input.getX - getX, stageHeight - Gdx.input.getY - getY)
        super.touchDown(event, x, y, pointer, button)
      }
      override def touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int): Unit = {
        val dx = x - getTouchDownX
        val dy = y - getTouchDownY
        if (dx * dx + dy * dy >= dragSensitivity) {
          cancel()
          setPosition(
            (Gdx.input.getX - pos._1).max(minX).min(xOffset),
            (stageHeight - Gdx.input.getY - pos._2).max(minY).min(yOffset)
          )
          super.touchDragged(event, x, y, pointer)
        }
      }
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = dispatcher(SelectTile(tileByPos(x, y)))
    })
    add(entityLayer)
  }
  private def tileByPos(x: Float, y: Float): Tile = Tile((x / (tileSize + 1)).toInt, (y / (tileSize + 1)).toInt)
}
object MapView extends UseAssets {
  val ASSET_SQUARE = "square.png"
  val ASSET_DIAMOND = "diamond.png"
  override def assets: Assets = Set(
    (ASSET_SQUARE, classOf[Texture]),
    (ASSET_DIAMOND, classOf[Texture])
  )
  case class SelectTile(tile: Tile) extends Action
  case class AddEntities(entityMap: Map[Tile, FieldEntity]) extends Action
}
