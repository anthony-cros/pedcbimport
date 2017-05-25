package pedcbimport.pipelines.copynumber

import enumeratum.{Enum, EnumEntry, PlayJsonEnum}
import pedcbimport.common.Wrapper._
import utils.Implicits.StringImplicits
import utils.Implicits.SeqImplicits

// ===========================================================================
case class Position(
    chromosome: Chromosome,
    position:   ChromosomePosition) {
    
  def formatValues =
    Seq(
      chromosome.format.prefixWith(CopyNumberConstants.ChromosomePrefix),
      position.start.toString,
      position.end  .toString)

}
  
// ---------------------------------------------------------------------------  
case class ChromosomePosition(
    start: Long,
    end:   Long) {
    
	// TODO: overlap size may be useful too

  def pair: (Long, Long) =
    (start, end)
  
  def overlapWith(that: ChromosomePosition) =
    utils.Overlap(
      this.pair,
      that.pair)
      
}

// ---------------------------------------------------------------------------
sealed trait CopyNumberType
    extends EnumEntry
  object CopyNumberType
      extends Enum[CopyNumberType]
         with PlayJsonEnum[CopyNumberType] {
    val values = findValues
      
    case object `-2` extends CopyNumberType
    case object `-1` extends CopyNumberType
    case object  `0` extends CopyNumberType
    case object  `1` extends CopyNumberType
    case object  `2` extends CopyNumberType
  }

// ---------------------------------------------------------------------------
sealed trait OverlapResult
      extends EnumEntry {
  
    def isDiscarded: Boolean =
      this == OverlapResult.Discarded

  }
  object OverlapResult
      extends Enum[OverlapResult]
         with PlayJsonEnum[OverlapResult] {
    val values = findValues
      
    case object `Discarded` extends OverlapResult
    case object `Full`      extends OverlapResult
    case object `Part`      extends OverlapResult
  }

// ---------------------------------------------------------------------------
case class Gene(
    hugoGeneSymbol:     HugoGeneSymbol,
    entrezGeneIdOpt:    Option[EntrezGeneId]) {

  def formatValues =
    Seq(
      hugoGeneSymbol.format,
      entrezGeneIdOpt.map(_.format).getOrElse(CopyNumberConstants.MissingEntrezGeneIdPlaceHolder))  
  
}

// ---------------------------------------------------------------------------
case class Result(
    sampleId:           SampleId,
    copyNumberPosition: Position,
    gene:               Gene,
    copyNumberType:     CopyNumberType,
    overlapResult:      OverlapResult) {
  
  def format: String =
      (Seq(sampleId.format) ++
      copyNumberPosition.formatValues ++ 
      gene              .formatValues ++ 
      Seq(
        copyNumberType.entryName,
        overlapResult .entryName.toLowerCase))
    .mkStringTab

}

// ===========================================================================
