package pedcbimport.pipelines.mutation

import enumeratum.{Enum, EnumEntry, PlayJsonEnum}

// ===========================================================================
object MutationConstants {

  val FirstLine = "#version 2.4"
  
  sealed trait DiscardedVariantType
      extends EnumEntry
    object DiscardedVariantType
        extends Enum[DiscardedVariantType]
           with PlayJsonEnum[DiscardedVariantType] {
      val values = findValues
      
      case object `Flank`  extends DiscardedVariantType
      case object `UTR`    extends DiscardedVariantType
      case object `Intron` extends DiscardedVariantType
      case object `RNA`    extends DiscardedVariantType
      case object `IGR`    extends DiscardedVariantType
    }  
    
  val UnknownHugoSymbolValue = "Unknown"
  
  val MissingEntrezGeneIdReplacementValue = "0" // TODO: point to cbio docs
  val MissingCosmicReplacementValue       = ""  // TODO: point to cbio docs
  
}

// ===========================================================================