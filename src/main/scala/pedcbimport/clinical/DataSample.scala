package pedcbimport.clinical

import utils.Tsv
import pedcbimport.common.Wrapper.SampleId

// ===========================================================================
object DataSample {
  
  val Header =
    Vector(
      Vector("#Patient identifier", "Sample identifier"),
      Vector("#STRING",             "STRING"),
      Vector("#1",                  "1"),
      Vector("#PATIENT_ID",         "SAMPLE_ID"))

  def apply(sampleIds: Seq[SampleId]): Tsv =
    Tsv(
      Header ++
      sampleIds
        .map { sampleId =>
          Vector(sampleId.format, sampleId.format) })  

}

// ===========================================================================
