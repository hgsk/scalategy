package scalategy.components

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor

trait Component {
  self: Actor =>
  def initialize(assetManager: AssetManager): Actor
}
