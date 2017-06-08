package gdxs

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{NinePatch => GdxNinePatch}
import gdxs.graphics.g2d.NinePatch

object Implicits {
  implicit class NinePatchBuilderWrapperFromTexture(texture: Texture) extends NinePatch.Builder(texture) {
    builder =>
    def build(all: Int): GdxNinePatch = builder.all(all).build()
  }
  implicit class AssetManagerWrapper(assetManager: AssetManager) {
    def nine(fileName: String): NinePatchBuilderWrapperFromTexture = new NinePatchBuilderWrapperFromTexture(assetManager.get[Texture](fileName))
  }
}
