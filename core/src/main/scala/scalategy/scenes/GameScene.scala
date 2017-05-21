package scalategy.scenes

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Image, Label}
import diode.{ActionHandler, ActionResult, Circuit}

import scalategy.AppSettings
import scalategy.components.MapView
import scalategy.components.MapView.SelectTile
import scalategy.shared.models.MapData

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
    val main = new Group
    val bitmapFont = assetManager.get[BitmapFont](ASSET_FONT)
    val squareTexture = assetManager.get[Texture](ASSET_SQUARE)
    val labelStyle = new LabelStyle(bitmapFont, new Color(0xffffffff))
    val headerBg = new Image(squareTexture)
    val footerBg = new Image(squareTexture)
    val headerLabel = new Label("Header", labelStyle)
    val circuit = new GameCircuit(GameModel(MapData.empty(30, 30)))

    circuit.subscribe(circuit.zoom(_.mapData))(mapData => println(mapData))
    headerBg.setBounds(0, 570, 800, 30)
    headerBg.setColor(0, 0, 0, 1)
    headerLabel.setPosition(10, 570)
    main.addActor(MapView(circuit.initialModel.mapData, circuit).initialize(assetManager))
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

  class GameCircuit(val initialModel: GameModel) extends Circuit[GameModel] {
    override protected def actionHandler: HandlerFunction = composeHandlers(mapHandler)
    val mapHandler = new ActionHandler(zoomTo(_.mapData)) {
      override protected def handle: PartialFunction[Any, ActionResult[GameModel]] = {
        case SelectTile(tile) => updated(value.copy(selectedTile = Set(tile)))
      }
    }
  }
  case class GameModel(mapData: MapData)
}

