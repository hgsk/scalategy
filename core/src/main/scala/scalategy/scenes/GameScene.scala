package scalategy.scenes

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.{Color, Texture}
import diode.{Action, ActionHandler, ActionResult, Circuit}
import gdxs.Implicits._
import gdxs.scenes.scene2d.Group
import gdxs.scenes.scene2d.ui.{Image, Label}

import scalategy.AppSettings
import scalategy.components.MapView
import scalategy.components.MapView.{AddEntities, SelectTile}
import scalategy.models.MapData
import scalategy.shared.EntityFactory
import scalategy.shared.models._
import scalategy.shared.utils.ManualClock

class GameScene()(implicit appSettings: AppSettings) extends Scene {
  scene =>
  import appSettings.mapSettings._
  import appSettings.{stageHeight, stageWidth}

  import GameScene._
  val circuit = GameCircuit(GameModel(MapData.empty(MapSize(30, 30)), new ManualClock(), Map.empty))
  val entityFactory = new EntityFactory
  override def assets: Assets = {
    super.assets ++ Set(
      (ASSET_CIRCLE, classOf[Texture]),
      (ASSET_SQUARE, classOf[Texture]),
      (ASSET_DIAMOND, classOf[Texture]),
      (ASSET_FONT, classOf[BitmapFont])
    ) ++ MapView.assets
  }
  override def enter(assetManager: AssetManager): Group = {
    val squareTexture = assetManager.texture(ASSET_SQUARE)
    val labelStyle = assetManager.labelStyle(ASSET_FONT, new Color(0xffffffff))
    val mapView = MapView(circuit.initialModel.mapData, circuit).pos(offsetX, offsetY)

    circuit.subscribe(circuit.zoom(_.mapData.selectedTiles)) {
      selectedTiles => mapView.updateSelectedTiles(selectedTiles.value)
    }
    circuit.subscribe(circuit.zoom(_.mapData.entityMap)) {
      entityMap => mapView.updateEntityMap(entityMap.value, assetManager)
    }
    circuit.subscribe(circuit.zoom(_.clock)) {
      clock =>
        circuit(FilterMovements(clock.value.now()))
        circuit(UpdateEntities(clock.value.now()))
    }

    Group()
      .add(mapView.initialize(assetManager))
      .add(Image(squareTexture).bounds(0, stageHeight - offsetY, stageWidth, offsetY).color(0, 0, 0, 1))
      .add(Label("Header", labelStyle).pos(10, stageHeight - offsetY))
      .add(Image(squareTexture).bounds(0, 0, stageWidth, offsetY).color(0, 0, 0, 1))
      .add(Label("Footer", labelStyle))
  }
  override def update(assetManager: AssetManager): Option[Scene] = {
    circuit(Past(1))
    None
  }
  override def exit(assetManager: AssetManager): Unit = ()
}

object GameScene {
  val ASSET_CIRCLE = "circle.png"
  val ASSET_SQUARE = "square.png"
  val ASSET_DIAMOND = "diamond.png"
  val ASSET_FONT = "caladea_bold.fnt"
  class GameCircuit(val initialModel: GameModel) extends Circuit[GameModel] {
    override protected def actionHandler: HandlerFunction = composeHandlers(mapHandler, clockHandler, movementHandler)
    def env: Environment = Environment(zoom(_.clock).value.now())
    val mapHandler = new ActionHandler(zoomTo(_.mapData)) {
      override protected def handle: PartialFunction[Any, ActionResult[GameModel]] = {
        case SelectTile(tile) => updated(value.copy(selectedTiles = Set(tile)))
        case AddEntities(entities) => updated(entities.foldLeft(value) { (acc, entity) => acc.copy(entityMap = acc.entityMap.updated(entity.entityId, entity)) })
        case UpdateEntities(now) => updated {
          val newEntityMap = zoom(_.movements)
            .value
            .foldLeft(value.entityMap) { case (entityMap, (entityId, movement)) =>
              entityMap.updated(entityId, entityMap(entityId)
                .asInstanceOf[MovableEntity]
                .copy(coordinates = movement.coordinates(now))
              )
            }
          value.copy(entityMap = newEntityMap)
        }
      }
    }
    val clockHandler = new ActionHandler(zoomTo(_.clock)) {
      override def handle: PartialFunction[Any, ActionResult[GameModel]] = {
        case Past(time) => updated(value.past(time))
      }
    }
    val movementHandler = new ActionHandler(zoomTo(_.movements)) {
      override def handle: PartialFunction[Any, ActionResult[GameModel]] = {
        case MoveTo(entity, target) =>
          updated(value.updated(entity.entityId, entity.moveTo(target)(env)))
        case FilterMovements(now) =>
          updated(value.filter { case (_, movement) => movement.finishedAt >= now })
      }
    }
  }
  object GameCircuit {
    def apply(initialModel: GameModel): GameCircuit = new GameCircuit(initialModel)
  }
  case class GameModel(mapData: MapData, clock: ManualClock, movements: Map[Long, Movement])
  case class Past(time: Long) extends Action
  case class MoveTo(entity: MovableEntity, target: Tile) extends Action
  case class UpdateEntities(now: Long) extends Action
  case class FilterMovements(now: Long) extends Action
}
