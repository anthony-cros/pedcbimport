package pedcbimport.references

import utils.Implicits.StringImplicits
import pedcbimport.common.Wrapper._
import pedcbimport.pipelines.copynumber.ChromosomePosition

// ===========================================================================
object mRNAs {
  
  def apply(mRnaTsvFilePath: String): Map[Chromosome, Map[ChromosomePosition, HugoGeneSymbol]] =
    mRnaTsvFilePath
      .path
      .streamTsvFile()
      .map(_.iterator)
      .map(RawMRna.apply)
      .map { rawMRna =>
         val chromosome =
           Chromosome(rawMRna.chromosome_number)
        
         val position =
           ChromosomePosition(
             rawMRna.start.toLong,
             rawMRna.end  .toLong)        
               
         (chromosome,
          position,
          HugoGeneSymbol(rawMRna.hugo_gene_symbol))
      }
      .groupBy { case (chromosome, _, _) => chromosome }
      .mapValues {
        _
          .map { case (_, position, hugoGeneSymbol) =>
            (position, hugoGeneSymbol) }
          .toMap }
  
}

// ===========================================================================
case class `RawMRna`(
  `chromosome_number`: String /* 1 */,
  `start`:             String /* 2 */,
  `end`:               String /* 3 */,
  `hugo_gene_symbol`:  String /* 4 */)

// ---------------------------------------------------------------------------
object `RawMRna` {
  def apply(it: Iterator[String]): RawMRna =
    RawMRna(
      `chromosome_number` = it.next() /* 1 */,
      `start`             = it.next() /* 2 */,
      `end`               = it.next() /* 3 */,
      `hugo_gene_symbol`  = it.next() /* 4 */) // looks we don't need the rest
  }
  
// ===========================================================================
