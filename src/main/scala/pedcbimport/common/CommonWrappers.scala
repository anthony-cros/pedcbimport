package pedcbimport.common

// ===========================================================================
trait Wrapper {
  
  val underlying: String
  require(underlying.nonEmpty, this)
  
  def format: String = underlying
}

// ---------------------------------------------------------------------------
object Wrapper {

  case class SampleId        (underlying: String) extends Wrapper  
  case class StudyShortName  (underlying: String) extends Wrapper
  case class StudyDisplayName(underlying: String) extends Wrapper  
  case class RdfsLabel       (underlying: String) extends Wrapper
  case class StudyId         (underlying: String) extends Wrapper  
  case class CancerTypeId    (underlying: String) extends Wrapper
  
  case class HugoGeneSymbol  (underlying: String) extends Wrapper 
  case class EntrezGeneId    (underlying: String) extends Wrapper
  
  // ===========================================================================
  case class StartPosition(value: Int) {
    require(value > 0, this)
    
    def format = value.toString
  }
  
  case class Chromosome(value: String) { // TODO: as enum?
    
  	def format = value
  	
    //require(Seq("X", "Y").contains(value) || value.toInt <= 23, value)
    /*
      TODO: vet
          
          1
          10
          11
          12
          13
          14
          15
          16
          17
          17_ctg5_hap1
          18
          19
          19_gl000209_random
          1_gl000191_random
          2
          20
          21
          22
          3
          4
          4_ctg9_hap1
          5
          6
          6_apd_hap1
          6_cox_hap2
          6_dbb_hap3
          6_mann_hap4
          6_mcf_hap5
          6_qbl_hap6
          6_ssto_hap7
          7
          7_gl000195_random
          8
          9
          Un_gl000213
          Un_gl000222
          Un_gl000223
          Un_gl000228
          X
          Y
          
          17_ctg5_hap1
          19_gl000209_random
          1_gl000191_random
          4_ctg9_hap1
          6_apd_hap1
          6_cox_hap2
          6_dbb_hap3
          6_mann_hap4
          6_mcf_hap5
          6_qbl_hap6
          6_ssto_hap7
          7_gl000195_random
          Un_gl000213
          Un_gl000222
          Un_gl000223
          Un_gl000228
       */
    
  }

}

// ===========================================================================

