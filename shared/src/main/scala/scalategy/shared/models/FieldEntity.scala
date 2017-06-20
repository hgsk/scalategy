package scalategy.shared.models

/**
  * フィールド上に存在するゲームオブジェクト
  */
trait FieldEntity {
  def entityId: Long
  def coordinates: Coordinates
}
