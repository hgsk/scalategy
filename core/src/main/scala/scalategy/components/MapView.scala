package scalategy.components

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.{Actor, InputEvent}
import diode.{Action, Dispatcher}
import gdxs.Implicits._
import gdxs.scenes.scene2d.Group

import scalategy.AppSettings
import scalategy.common.UseAssets
import scalategy.models.MapData
import scalategy.shared.models.{FieldEntity, MovableEntity, Tile}

class MapView(initialMapData: MapData, dispatcher: Dispatcher)(implicit appSettings: AppSettings) extends Group with Component {
  import appSettings.mapSettings._
  import appSettings.stageHeight

  import MapView._
  import scalategy.shared.Constants.tileSize
  val entityLayer = Group()
  private var tiles: Map[Tile, Actor] = Map.empty
  private var mapData: MapData = initialMapData
  def updateSelectedTiles(selectedTiles: Set[Tile]): Unit = {
    mapData.selectedTiles.map(tiles).foreach(_.setColor(1, 1, 1, 1))
    selectedTiles.map(tiles).foreach(_.setColor(1, 0, 0, 1))
    mapData = mapData.copy(selectedTiles = selectedTiles)
  }
  def updateEntityMap(entities: Map[Long, FieldEntity], assetManager: AssetManager): Unit = {
    entityLayer.clear()
    for ((_, entity) <- entities) {
      entityLayer.addActor(entityToImage(entity, assetManager))
    }
  }
  def entityToImage(entity: FieldEntity, assetManager: AssetManager): Image = {
    entity match {
      case _: MovableEntity =>
        assetManager
          .image(ASSET_CIRCLE)
          .bounds(entity.coordinates.modXY(_ + tileSize / 4), tileSize / 2)
          .color(0, 0, .9f, 1)
      case _ =>
        assetManager
          .image(ASSET_DIAMOND)
          .bounds(entity.coordinates.modX(_ + tileSize / 4), tileSize / 2, tileSize)
          .color(.9f, .9f, 0, 1)
    }
  }
  override def initialize(assetManager: AssetManager): Actor = {
    val mapSizeX = mapData.mapSize.x
    val mapSizeY = mapData.mapSize.y
    val minX = ((tileSize + 1) * mapSizeX - mapWidth - offsetX) * -1
    val minY = ((tileSize + 1) * mapSizeY - mapHeight - offsetY) * -1
    for (x <- 0 until mapSizeX; y <- 0 until mapSizeY) {
      val square = assetManager.image(ASSET_SQUARE).bounds(Tile(x, y).toCoordinates, tileSize)
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
            (Gdx.input.getX - pos._1).max(minX).min(offsetX),
            (stageHeight - Gdx.input.getY - pos._2).max(minY).min(offsetY)
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
  val ASSET_CIRCLE = "circle.png"
  def apply(initialMapData: MapData, dispatcher: Dispatcher)(implicit appSettings: AppSettings): MapView = new MapView(initialMapData, dispatcher)
  override def assets: Assets = Set(
    (ASSET_SQUARE, classOf[Texture]),
    (ASSET_DIAMOND, classOf[Texture])
  )
  case class SelectTile(tile: Tile) extends Action
  case class AddEntities(entities: Seq[FieldEntity]) extends Action
}
