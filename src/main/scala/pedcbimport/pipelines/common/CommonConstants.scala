package pedcbimport.pipelines.common

import pedcbimport.DataType

// ===========================================================================
object CommonConstants {
  
  val Extensions: Map[DataType, String] =
    Map(
      DataType.Mutation   -> ".maf",
      DataType.CopyNumber -> ".bam_CNVs",
      DataType.Expression -> ".genes.results")
  
}

// ===========================================================================
