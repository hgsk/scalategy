package scalategy.scenes

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import diode.{ActionHandler, ActionResult, Circuit}
import gdxs.scenes.scene2d.Group
import gdxs.scenes.scene2d.ui.Image

import scalategy.AppSettings
import scalategy.components.MapView
import scalategy.components.MapView.{AddEntities, SelectTile}
import scalategy.models.MapData
import scalategy.shared.models.{FieldEntity, MapSize, Tile}

class GameScene()(implicit val appSettings: AppSettings) extends Scene {
  scene =>
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
    val main = Group()
    val bitmapFont = assetManager.get[BitmapFont](ASSET_FONT)
    val squareTexture = assetManager.get[Texture](ASSET_SQUARE)
    val labelStyle = new LabelStyle(bitmapFont, new Color(0xffffffff))
    val headerBg = Image(squareTexture)
    val footerBg = Image(squareTexture)
    val headerLabel = new Label("Header", labelStyle)
    val circuit = GameCircuit(GameModel(MapData.empty(MapSize(30, 30))))
    val mapView = MapView(circuit.initialModel.mapData, circuit)

    circuit.subscribe(circuit.zoom(_.mapData.selectedTiles)) {
      selectedTiles => mapView.updateSelectedTiles(selectedTiles.value)
    }
    circuit.subscribe(circuit.zoom(_.mapData.entityMap)) {
      entityMap => mapView.updateEntityMap(entityMap.value, assetManager)
    }

    headerBg.setBounds(0, 570, 800, 30)
    headerBg.setColor(0, 0, 0, 1)
    headerLabel.setPosition(10, 570)
    main.addActor(mapView.initialize(assetManager))
    main.addActor(headerBg)
    main.addActor(headerLabel)

    footerBg.setBounds(0, 0, 800, 30)
    footerBg.setColor(0, 0, 0, 1)
    main.addActor(footerBg)
    main.addActor(new Label("Footer", labelStyle))

    // add entity test
    circuit(AddEntities(Map(Tile(1, 1) -> new FieldEntity {})))

    main
  }
  override def update(assetManager: AssetManager): Unit = ()
  override def exit(assetManager: AssetManager): Unit = ()

  class GameCircuit(val initialModel: GameModel) extends Circuit[GameModel] {
    override protected def actionHandler: HandlerFunction = composeHandlers(mapHandler)
    val mapHandler = new ActionHandler(zoomTo(_.mapData)) {
      override protected def handle: PartialFunction[Any, ActionResult[GameModel]] = {
        case SelectTile(tile) => updated(value.copy(selectedTiles = Set(tile)))
        case AddEntities(entityMap) => updated(value.copy(entityMap = entityMap))
      }
    }
  }
  object GameCircuit {
    def apply(initialModel: GameModel): GameCircuit = new GameCircuit(initialModel)
  }
  case class GameModel(mapData: MapData)
}

