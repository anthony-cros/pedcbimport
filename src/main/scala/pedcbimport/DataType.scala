package pedcbimport

import enumeratum.{Enum, EnumEntry, PlayJsonEnum}

// ===========================================================================
sealed trait DataType
    extends EnumEntry
  object DataType
      extends Enum[DataType]
         with PlayJsonEnum[DataType] {
    val values = findValues
      
    case object `Mutation`   extends DataType
    case object `CopyNumber` extends DataType
    case object `Expression` extends DataType
  }  

// ===========================================================================
