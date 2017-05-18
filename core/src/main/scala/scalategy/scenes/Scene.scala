package scalategy.scenes

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Group

import scalategy.common.UseAssets

trait Scene extends UseAssets {
  override def assets: Assets = Set.empty
  def enter(assetManager: AssetManager): Group
  def update(assetManager: AssetManager): Unit
  def exit(assetManager: AssetManager): Unit
}
