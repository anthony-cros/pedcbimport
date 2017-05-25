package pedcbimport.pipelines.expression

import pedcbimport.pipelines.common.Wrapper._

// ===========================================================================
case class Expression(
    hugoGeneId:   HugoGeneSymbol,
    entrezGeneId: Option[EntrezGeneId],
    data:         Map[String, Double]) { // values are FPKMs or z-scores
  
  require(data.nonEmpty, this)
  
  // either there is no NaN, or they all are (as a result of dividing by a zero std in the z-score computation)
  require(
    !data.values.exists(_.isNaN) || 
     data.values.forall(_.isNaN),
    this)
  
  def asRow(sampleIds: Vector[String]): Vector[String] =
    Vector(
      hugoGeneId.format,
      entrezGeneId.map(_.format).getOrElse("NA")) ++
    sampleIds
      .map(data) // TODO: guaranteed always a FPKM value for all samples?
      .map(value =>
        if (value.isNaN) "NA"
        else             f"${value}%5f") // %5f ok?
      
}

// ===========================================================================
