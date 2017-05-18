package scalategy

import com.badlogic.gdx.Net.{HttpMethods, HttpResponseListener}
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.{Color, GL20, Texture}
import com.badlogic.gdx.net.HttpRequestBuilder
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Image, Label, Table}
import com.badlogic.gdx.scenes.scene2d.{Group, Stage}
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.{ApplicationListener, Gdx, Net}
import upickle.default._

import scala.concurrent.{Future, Promise}

class AppListener()(implicit appSettings: AppSettings) extends ApplicationListener {
  import appSettings._
  lazy val assetManager = new AssetManager()
  lazy val stage: Stage = new Stage(new FitViewport(stageWidth, stageHeight))
  private var initialized: Boolean = false
  private var currentScene: Option[Scene] = None
  def changeScene(scene: Scene): Unit = {
    currentScene.map(_.assets).foreach(_.foreach { case (file, _) =>
      assetManager.unload(file)
    })
    currentScene.foreach(_.exit(assetManager))
    stage.clear()
    currentScene = Some(scene)
    initialized = false
    scene.assets.foreach { case (file, clazz) =>
      assetManager.load(file, clazz)
    }
  }
  override def create(): Unit = {
    Gdx.input.setInputProcessor(stage)
    changeScene(new GameScene)
  }
  override def render(): Unit = {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    Gdx.gl.glClearColor(0, 0, 0, 1)

    if (!initialized) {
      if (assetManager.update()) {
        currentScene.foreach(scene => stage.addActor(scene.enter(assetManager)))
        initialized = true

      } else {
        // Now Loading...
      }
    } else {
      currentScene.foreach(_.update(assetManager))
    }

    stage.act(Gdx.graphics.getDeltaTime)
    stage.draw()
  }
  override def dispose(): Unit = {
    assetManager.dispose()
    stage.dispose()
  }
  override def resize(width: Int, height: Int): Unit = {
    stage.getViewport.update(width, height)
  }
  override def resume(): Unit = ()
  override def pause(): Unit = ()
}

class GameScene extends Scene {
  val ASSET_CIRCLE = "circle.png"
  val ASSET_SQUARE = "square.png"
  val ASSET_DIAMOND = "diamond.png"
  val ASSET_FONT = "caladea_bold.fnt"
  override def assets: Seq[(String, Class[_])] = {
    super.assets ++ Seq(
      (ASSET_CIRCLE, classOf[Texture]),
      (ASSET_SQUARE, classOf[Texture]),
      (ASSET_DIAMOND, classOf[Texture]),
      (ASSET_FONT, classOf[BitmapFont])
    )
  }
  override def enter(assetManager: AssetManager): Group = {
    val circleTexture = assetManager.get[Texture](ASSET_CIRCLE)
    val main = new Group
    for (_ <- 1 to 100) {
      val circleImage = new Image(circleTexture)
      circleImage.setColor(Math.random().toFloat, Math.random().toFloat, Math.random().toFloat, 1)
      circleImage.setSize(10, 10)
      circleImage.setPosition((Math.random() * 1000).toFloat, (Math.random() * 1000).toFloat)
      main.addActor(circleImage)
    }
    val bitmapFont = assetManager.get[BitmapFont](ASSET_FONT)
    val table = new Table()
    val labelStyle = new LabelStyle(bitmapFont, new Color(0xffffffff))
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

trait Scene {
  def assets: Seq[(String, Class[_])] = Seq.empty
  def enter(assetManager: AssetManager): Group
  def update(assetManager: AssetManager): Unit
  def exit(assetManager: AssetManager): Unit
}

object AutowireClient extends autowire.Client[String, Reader, Writer] {
  override def doCall(req: Request): Future[String] = {
    val builder = new HttpRequestBuilder
    val promise = Promise[String]()
    builder
      .newRequest()
      .method(HttpMethods.POST)
      .url("http://127.0.0.1:9000/api/" + req.path.mkString("/"))
      .content(upickle.default.write(req.args))
    Gdx.net.sendHttpRequest(builder.build(), new HttpResponseListener {
      override def failed(t: Throwable): Unit = promise.failure(t)
      override def handleHttpResponse(httpResponse: Net.HttpResponse): Unit = promise.success(httpResponse.getResultAsString)
      override def cancelled(): Unit = promise.failure(CanceledException)
    })
    promise.future
  }
  override def read[Result: Reader](p: String): Result = upickle.default.read[Result](p)
  override def write[Result: Writer](r: Result): String = upickle.default.write(r)
}

object CanceledException extends Throwable
