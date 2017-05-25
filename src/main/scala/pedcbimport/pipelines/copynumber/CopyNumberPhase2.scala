package pedcbimport.pipelines.copynumber

import utils.Implicits.SeqImplicits
import utils.Implicits.IterableImplicits
import utils.Implicits.StringImplicits
import utils.Implicits.OptionImplicits
import utils.Tsv
import pedcbimport.common.Wrapper.SampleId

// ===========================================================================
object CopyNumberPhase2 {
  
  def apply(results: Iterable[Result]): Tsv = {
    
    val sampleIds: Seq[SampleId] =
      results.map(_.sampleId).toSeq
          
    val header: Vector[String] =
      (Vector(
        CopyNumberConstants.HugoGeneSymbolHeader,
        CopyNumberConstants.EntrezGeneIdHeader) ++
      results.map(_.sampleId.format))
    
    val rows: Iterable[Vector[String]] = 
      results
        .groupBy(_.gene)
        .mapValues(_.toSeq.indexBy(_.sampleId))
        .mapValues { sample2Result =>
          sampleIds
            .map(sample2Result.get)
            .map(
              _
                .map(_.copyNumberType)
                .getOrElse(CopyNumberType.`0`)) }
        .map { case (gene, sampleValues) =>
          gene.formatValues ++
          sampleValues.map(_.entryName) }
        .map(_.toVector)
     
    Tsv(
      Seq(header) ++
      rows)  
  }
  
}

// ===========================================================================
