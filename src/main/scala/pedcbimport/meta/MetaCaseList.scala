package pedcbimport.meta

import pedcbimport.Enums
import pedcbimport.pipelines.common.Wrapper._
import play.api.libs.json.Json
import enumeratum.{Enum, EnumEntry, PlayJsonEnum}
import utils.Implicits.StringImplicits

// ===========================================================================
case class CaseList( // case list is "meta" by nature
		cancer_study_identifier: String,
		case_list_description:   String,
		case_list_ids:           String,
		case_list_name:          String, 
		stable_id:               String) {
    
  def format: String =
    utils.Utils.formatJsValue(
      Json.toJson(
        this)(
        Json.writes[CaseList]))

  def writeCaseList(
      parentDirPath: String,
      studyId: StudyId, samples: Seq[SampleId], suffix: Enums.case_list_suffix) {
    s"${parentDirPath}/case_lists".path.mkdirs
    
    s"${parentDirPath}/case_lists/cases${suffix.entryName}.txt"
      .path
      .writeFile(
        format)
  }
  
}

// ---------------------------------------------------------------------------
object CaseList {
  
  def apply(studyId: StudyId, samples: Seq[SampleId], suffix: Enums.case_list_suffix): CaseList =
    CaseList(
    	cancer_study_identifier = studyId.format,
    	case_list_ids           = samples.map(_.format).mkString("\t"),
    	case_list_description   = s"All tumor samples (${samples.size} samples)",
    	case_list_name          = "All Tumors vs Normals", 
    	stable_id               = suffix.entryName.prefixWith(studyId.format))    
		
}

// ===========================================================================
