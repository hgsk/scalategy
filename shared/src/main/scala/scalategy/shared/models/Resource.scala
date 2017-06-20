package scalategy.shared.models

import scalategy.shared.models.ResourceTypes._

/**
  * 資源
  */
final case class Resource(resourceType: ResourceType, amount: Int)
object Resource {
  def food(amount: Int) = Resource(Food, amount)
  def wood(amount: Int) = Resource(Wood, amount)
  def soil(amount: Int) = Resource(Soil, amount)
  def water(amount: Int) = Resource(Water, amount)
  def gold(amount: Int) = Resource(Gold, amount)
}
object ResourceTypes {
  sealed trait ResourceType
  case object Food extends ResourceType // 食料
  case object Wood extends ResourceType // 木材
  case object Soil extends ResourceType // 土
  case object Water extends ResourceType // 水
  case object Gold extends ResourceType // 金
  val all: Set[ResourceType] = Set(Food, Wood, Soil, Water, Gold)
  def map[B](f: (ResourceType) => B): Set[B] = all.map(f)
}
