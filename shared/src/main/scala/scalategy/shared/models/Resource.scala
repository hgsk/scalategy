package scalategy.shared.models

/**
  * 資源
  */
sealed trait Resource
sealed trait ResourceType
case object Food extends ResourceType // 食料
case object Wood extends ResourceType // 木材
