package pedcbimport.meta

import play.api.libs.json.Json
import pedcbimport.common.Wrapper._
import pedcbimport.Enums
import utils.Implicits.StringImplicits
import utils.Tsv

// ===========================================================================
object MetaSample {
    
  def toMeta(study_id: StudyId) =
    meta(
      cancer_study_identifier = study_id.format,
      genetic_alteration_type = "CLINICAL",
      datatype                = "SAMPLE_ATTRIBUTES",
      data_filename           = "data_clinical_samples.txt")
    
  def toMetaContent(study_id: StudyId) =
    utils.Utils.formatJsValue(Json.toJson(toMeta(study_id))(Json.writes[meta]))
      
  def writeMetaFile(parentDirPath: String, study_id: StudyId) =
    s"${parentDirPath}/meta_${Enums.stable_id.cna.entryName}.txt"
      .path
      .writeFile(
        toMetaContent(study_id))  
        
  // ---------------------------------------------------------------------------
  case class meta(
      cancer_study_identifier: String, // ganglio_cbttc_0001
      genetic_alteration_type: String, // CLINICAL
      datatype:                String, // SAMPLE_ATTRIBUTES
      data_filename:           String) { // data_clinical_samples.txt
      
    implicit val JsonWrites = Json.writes[meta]
  
    def format: String =
      utils.Utils.formatJsValue(
          Json.toJson[meta](this))
  }
 
}

// ===========================================================================
