package scalategy.shared.models

/**
  * 施設
  */
sealed trait Facility extends FieldEntity
sealed trait FacilityType
case object Bridgehead extends FacilityType // 拠点
case object Farm extends FacilityType // 畑
