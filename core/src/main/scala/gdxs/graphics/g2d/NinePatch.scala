package gdxs.graphics.g2d

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{NinePatch => GdxNinePatch}

object NinePatch {
  def texture(texture: Texture): Builder = Builder(texture)
  case class Builder(
    texture: Texture,
    left: Int = 0,
    right: Int = 0,
    top: Int = 0,
    bottom: Int = 0
  ) {
    def all(i: Int): Builder = copy(left = i, right = i, top = i, bottom = i)
    def apply(texture: Texture): Builder = copy(texture = texture)
    def texture(texture: Texture): Builder = copy(texture = texture)
    def build(): GdxNinePatch = new GdxNinePatch(texture, left, right, top, bottom)
  }
}
