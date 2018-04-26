package pedcbimport.pipelines.expression

import utils.Implicits.StringImplicits
import utils.Implicits.IterableImplicits
import pedcbimport.common.Wrapper._
import pedcbimport.common.CommonUtils
import pedcbimport.references.GeneLookup
import utils.Implicits.ClassImplicits
import utils.Tsv

// ===========================================================================
/* this is a port of https://github.com/d3b-center/d3b-maf2pedcbio/blob/f0806f2/convert-to-cbio-rnaseq/trans_to_cbio_RSEM.pl */
object ExpressionData {
  
  def apply(
        geneKvpsFilePath:  String,        
        samplesDirPath: String)
      : (Tsv, Tsv) = {

    val geneLookup: Map[HugoGeneSymbol, EntrezGeneId] =
      GeneLookup(geneKvpsFilePath)    
  
    val RawalSampleRawExpressionFiles: List[String] =
      CommonUtils.expressionSampleFilePaths(samplesDirPath)
       
     val actualHeaderFields: Vector[String] =        
       RawalSampleRawExpressionFiles
         .head
         .path
         .readHeaderFields

     assert(
        classOf[RawExpression].fieldNames == actualHeaderFields,
       (classOf[RawExpression].fieldNames,   actualHeaderFields))
     
     val sampleIds: Vector[String] =
       RawalSampleRawExpressionFiles.map(_.path.basename).toVector
     
     val expressionRaws: Iterable[Expression] =
       this.expressions(
         geneLookup,
         RawalSampleRawExpressionFiles)
       
     val expressionZscores: Iterable[Expression] = 
       expressionsZscore(expressionRaws)
  

     (Tsv(expressionRaws   .map(_.asRow(sampleIds))),
      Tsv(expressionZscores.map(_.asRow(sampleIds))))
  }

  // ===========================================================================
  private def expressions(
        geneLookup: Map[HugoGeneSymbol, EntrezGeneId],
        RawalSampleRawExpressionFiles: List[String])
      : Iterable[Expression] =      
     RawalSampleRawExpressionFiles
       .flatMap { RawalSampleRawExpressionFilePath =>
           RawalSampleRawExpressionFilePath
             .path
             .streamTsvFile()
             .drop(1) // drop header
             .map(_.toIterator)
             .map(RawExpression.apply)              
             .map { RawExpression =>
               (HugoGeneSymbol(RawExpression.gene_id),
                RawalSampleRawExpressionFilePath.path.basename,
                RawExpression.FPKM.toDouble) } }       
       .groupBy { case (geneId, _, _) => geneId }
       .map { case (geneId, groupedValues) =>
         Expression(
           hugoGeneId   = geneId,
           entrezGeneId = geneLookup.get(geneId),
           data =
             groupedValues
               .map { case (_, sampleId, fpkm) =>
                 sampleId -> fpkm }
               .toMap) }

  // ---------------------------------------------------------------------------  
  private def expressionsZscore(expressionsRaw: Iterable[Expression]): Iterable[Expression] =   
     expressionsRaw
       .map { expression =>
         val preProcessedValues =
            expression
              .data                       // sample FPKM values                
              .mapValues(_ + 1)           // add 1
              .mapValues(Math.log)        // compute log10 of value
              .mapValues(_ / Math.log(2)) // divide by ln(2) to get log2 (see base-changing identity)
   
         val (mean: Double, std: Double) = {             
            val doubles: Array[Double] =
              preProcessedValues.values.toArray
               
            val mean: Double =
              org.apache.commons.math3.stat.StatUtils.mean(doubles)
      
            val std: Double = // may be 0
              Math.sqrt(
               org.apache.commons.math3.stat.StatUtils.variance(doubles))
          
           (mean, std)
          }
  
         val zScores: Map[String, Double] =
           preProcessedValues
             .mapValues(_ - mean) // subtract mean
             .mapValues(_ / std)  // divide by standard deviation
          
         assert(
           !zScores.values.exists(_.isInfinity), // may be NaN however, if std was 0
           (expression, preProcessedValues))              
              
         expression.copy(data = zScores) // replace raw FPKM with z-score counterparts
       }
     
}

// ===========================================================================
