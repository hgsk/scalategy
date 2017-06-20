package scalategy.common

trait UseAssets {
  type Asset = (String, Class[_])
  type Assets = Set[Asset]
  def assets: Assets
}
