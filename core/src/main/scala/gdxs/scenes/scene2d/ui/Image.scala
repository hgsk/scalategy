package gdxs.scenes.scene2d.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{NinePatch, TextureRegion}
import com.badlogic.gdx.scenes.scene2d.ui.{Skin, Image => GdxImage}
import com.badlogic.gdx.scenes.scene2d.utils.{Drawable, NinePatchDrawable}
import com.badlogic.gdx.utils.{Align, Scaling}

object Image {
  def apply(patch: NinePatch) = new GdxImage(new NinePatchDrawable(patch), Scaling.stretch, Align.center)
  def apply(drawable: Drawable, scaling: Scaling, align: Int): GdxImage = new GdxImage(drawable, scaling, align)
  def apply(region: TextureRegion): GdxImage = new GdxImage(region)
  def apply(skin: Skin, drawableName: String): GdxImage = new GdxImage(skin, drawableName)
  def apply(drawable: Drawable): GdxImage = new GdxImage(drawable)
  def apply(drawable: Drawable, scaling: Scaling): GdxImage = new GdxImage(drawable, scaling)
  def apply(texture: Texture): GdxImage = new GdxImage(texture)
}
