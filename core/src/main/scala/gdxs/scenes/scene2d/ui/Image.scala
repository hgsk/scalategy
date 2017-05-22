package gdxs.scenes.scene2d.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{NinePatch, TextureRegion}
import com.badlogic.gdx.scenes.scene2d.ui.{Skin, Image => GdxImage}
import com.badlogic.gdx.scenes.scene2d.utils.{Drawable, NinePatchDrawable, TextureRegionDrawable}
import com.badlogic.gdx.utils.{Align, Scaling}

class Image(drawable: Drawable, scaling: Scaling, align: Int) extends GdxImage(drawable, scaling, align) {
  def this(patch: NinePatch) = this(new NinePatchDrawable(patch), Scaling.stretch, Align.center)
  def this(region: TextureRegion) = this(new TextureRegionDrawable(region), Scaling.stretch, Align.center)
  def this(skin: Skin, drawableName: String) = this(skin.getDrawable(drawableName), Scaling.stretch, Align.center)
  def this(drawable: Drawable) = this(drawable, Scaling.stretch, Align.center)
  def this(drawable: Drawable, scaling: Scaling) = this(drawable, scaling, Align.center)
  def this(texture: Texture) = this(new TextureRegionDrawable(new TextureRegion(texture)))
}
object Image {
  def apply(drawable: Drawable, scaling: Scaling, align: Int): Image = new Image(drawable, scaling, align)
  def apply(region: TextureRegion): Image = new Image(region)
  def apply(skin: Skin, drawableName: String): Image = new Image(skin, drawableName)
  def apply(drawable: Drawable): Image = new Image(drawable)
  def apply(drawable: Drawable, scaling: Scaling): Image = new Image(drawable, scaling)
  def apply(texture: Texture): Image = new Image(texture)
}
