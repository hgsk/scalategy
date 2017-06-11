package scalategy.shared.models

import scala.util.Try
import scalategy.shared.models.ResourceTypes.ResourceType

case class Stock private(resources: Map[ResourceType, Int], capacities: Map[ResourceType, Option[Int]]) {
  def amount(resourceType: ResourceType): Int = resources(resourceType)
  def limit(resourceType: ResourceType): Int = capacities.get(resourceType).flatten.getOrElse(Int.MaxValue)
  def cut(resourceType: ResourceType): Stock = copy(resources.updated(resourceType, amount(resourceType).min(limit(resourceType))))
  def add(resource: Resource): Stock = add(resource.resourceType, resource.amount)
  def add(resourceType: ResourceType, delta: Int): Stock = {
    require(delta > 0)
    copy(resources.updated(resourceType, (amount(resourceType) + delta).min(limit(resourceType))))
  }
  def consume(resource: Resource): Try[Stock] = consume(resource.resourceType, resource.amount)
  def consume(resourceType: ResourceType, delta: Int): Try[Stock] = Try {
    require(amount(resourceType) >= delta)
    copy(resources.updated(resourceType, amount(resourceType) - delta))
  }
  def updateCapacity(resourceType: ResourceType, limit: Int): Stock = {
    require(limit > 0)
    copy(capacities = capacities.updated(resourceType, Some(limit)))
      .cut(resourceType)
  }
}
object Stock {
  def apply(): Stock = Stock(0)
  def apply(amount: Int): Stock = Stock(amount, None)
  def apply(amount: Int, limit: Int): Stock = Stock(amount, Some(limit))
  def apply(amount: Int, limit: Option[Int]): Stock = Stock(ResourceTypes.map(_ -> amount).toMap, ResourceTypes.map(_ -> limit).toMap)
}
