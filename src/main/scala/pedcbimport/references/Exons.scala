package pedcbimport.references

import utils.Implicits.StringImplicits
import pedcbimport.pipelines.common.Wrapper._
import pedcbimport.pipelines.copynumber.ChromosomePosition
import pedcbimport.pipelines.copynumber.CopyNumberConstants

// ===========================================================================
object Exons {
  
  def apply(exonsBedFilePath: String): Map[Chromosome, Map[ChromosomePosition, HugoGeneSymbol]] =
    exonsBedFilePath
      .path
      .streamTsvFile()
      .map(_.iterator)
      .map(RawExon.apply)
      .map { rawExon =>
         val chromosome =
           Chromosome(
             rawExon.chromosome_name
               .removePrefix(CopyNumberConstants.ChromosomePrefix.prefixWithCaret.regex))
        
         val position =
           ChromosomePosition(
             rawExon.start.toLong,
             rawExon.end  .toLong)        
               
         (chromosome,
          position,
          HugoGeneSymbol(rawExon.hugo_gene_symbol))
      }
      .groupBy { case (chromosome, _, _) => chromosome }
      .mapValues {
        _
          .map { case (_, position, hugoGeneSymbol) =>
            (position, hugoGeneSymbol) }
          .toMap }
      
}

// ===========================================================================
case class `RawExon`(
  `chromosome_name`:  String /* 1 */,
  `start`:            String /* 2 */,
  `end`:              String /* 3 */,
  `hugo_gene_symbol`: String /* 4 */)

// ---------------------------------------------------------------------------
object `RawExon` {
  def apply(it: Iterator[String]): RawExon =
    RawExon(
      `chromosome_name`  = it.next() /* 1 */,
      `start`            = it.next() /* 2 */,
      `end`              = it.next() /* 3 */,
      `hugo_gene_symbol` = it.next() /* 4 */)
  }

// ===========================================================================
