package gdxs

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.{NinePatch => GdxNinePatch}
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.net.HttpRequestBuilder
import com.badlogic.gdx.scenes.scene2d.{Actor, Group}
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Label, Image => GdxImage}
import gdxs.graphics.g2d.NinePatch
import gdxs.net.HttpResponse
import gdxs.scenes.scene2d.ui.Image

import scala.concurrent.Future
import scalategy.shared.models.Coordinates

object Implicits {
  implicit class NinePatchBuilderWrapperFromTexture(texture: Texture) extends NinePatch.Builder(texture) {
    builder =>
    def build(all: Int): GdxNinePatch = builder.all(all).build()
  }
  implicit class TextureWrapper(texture: Texture) {
    def filter(minFilter: TextureFilter, magFilter: TextureFilter): Texture = {
      texture.setFilter(minFilter, magFilter)
      texture
    }
  }
  implicit class AssetManagerWrapper(assetManager: AssetManager) {
    def texture(fileName: String): Texture = assetManager.get(fileName)
    def image(fileName: String): GdxImage = Image(assetManager.texture(fileName))
    def nine(fileName: String): NinePatchBuilderWrapperFromTexture = new NinePatchBuilderWrapperFromTexture(assetManager.get[Texture](fileName))
    def labelStyle(fileName: String, color: Color): LabelStyle = new LabelStyle(assetManager.get(fileName), color)
  }
  implicit class HttpRequestBuilderWrapper(httpRequestBuilder: HttpRequestBuilder) {
    def send(): Future[HttpResponse] = gdxs.net.sendHttpRequest(httpRequestBuilder.build())
  }
  trait ActorWrapper[T <: Actor] {
    val actor: T
    def pos(x: Float, y: Float): T = {
      actor.setPosition(x, y)
      actor
    }
    def pos(coordinates: Coordinates): T = {
      actor.setPosition(coordinates.x, coordinates.y)
      actor
    }
    def size(width: Float, height: Float): T = {
      actor.setSize(width, height)
      actor
    }
    def bounds(x: Float, y: Float, width: Float, height: Float): T = {
      actor.setBounds(x, y, width, height)
      actor
    }
    def bounds(coordinates: Coordinates, width: Float, height: Float): T = bounds(coordinates.x, coordinates.y, width, height)
    def bounds(coordinates: Coordinates, size: Float): T = bounds(coordinates.x, coordinates.y, size, size)
    def color(r: Float, g: Float, b: Float, a: Float): T = {
      actor.setColor(r, g, b, a)
      actor
    }
    def color(color: Color): T = {
      actor.setColor(color)
      actor
    }
  }
  implicit class LabelWrapper(val actor: Label) extends ActorWrapper[Label]
  implicit class ImageWrapper(val actor: GdxImage) extends ActorWrapper[GdxImage]
  implicit class GroupWrapper[T <: Group](val actor: T) extends ActorWrapper[T]
}
