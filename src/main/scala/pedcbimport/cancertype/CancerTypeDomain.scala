package pedcbimport.cancertype

import pedcbimport.common.Wrapper._
import pedcbimport.Enums
import utils.Implicits.StringImplicits
import play.api.libs.json.Json
  
// ===========================================================================   
case class CancerType(
      _id:                       CancerTypeId,
      study:                     StudyId,
      `rdfs:label`:              String,
      `clinical_trial_keywords`: Seq[String],
      `dedicated_css_color`:     X11COLOR,    
      parent_type_of_cancer:     Option[String]) {
    
    private val genetic_alteration_type =
      Enums.genetic_alteration_type.CANCER_TYPE.entryName
  
    def toMeta =
      CancerType.meta(
        genetic_alteration_type = genetic_alteration_type,
        datatype                = genetic_alteration_type,
        data_filename           = genetic_alteration_type.surroundWith("data_", ".txt"))            
  
    def toData =
      CancerType.data(
        type_of_cancer          = _id.format,
        name                    = `rdfs:label`,
        clinical_trial_keywords = clinical_trial_keywords.mkString(","), 
        dedicated_color         = dedicated_css_color.entryName,
        parent_type_of_cancer   = parent_type_of_cancer.getOrElse("tissue"))      
    
    def formatMeta: String =
      utils.Utils.formatJsValue(
        Json.toJson(toMeta)(Json.writes[CancerType.meta]))

    def formatData: String =
      utils.Utils.formatJsValue(
        Json.toJson(toData)(Json.writes[CancerType.data]))

    def writeFiles(parentDirPath: String) {
      writeMetaFile(parentDirPath)
      writeDataFile(parentDirPath)
    }
        
    def writeMetaFile(parentDirPath: String) {
      s"${parentDirPath}/meta_${genetic_alteration_type}.txt"
        .path
        .writeFile(formatMeta)
    }
    
    def writeDataFile(parentDirPath: String) {
      s"${parentDirPath}/data_${genetic_alteration_type}.txt"
        .path
        .writeFile(formatData)
    }

  }

  // ---------------------------------------------------------------------------
  object CancerType {
    
    import pedcbimport.common.CommonPlayJson._
    def fromJson(json: String): CancerType =
      Json
        .fromJson[CancerType](
          Json.parse(json))
        .get
    
    case class meta(
      genetic_alteration_type: String,
      datatype:                String,
      data_filename:           String)

    case class data( 
        type_of_cancer: String, // short name; "The cancer type abbreviation, e.g., “brca”." 
        name: String, // The name of the cancer type, e.g., “Breast Invasive Carcinoma”.
        clinical_trial_keywords: String, // TODO: val; A comma separated list of keywords used to identify this study, e.g., “breast,breast invasive”.
        dedicated_color: String, // CSS color name of the color associated with this cancer study, e.g., “HotPink”. See this list for supported names, and follow the awareness ribbons color schema. This color is associated with the cancer study on various web pages within the cBioPortal.
        parent_type_of_cancer: String) // None is "tissue", eg root of oncotree; The type_of_cancer field of the cancer type of which this is a subtype, e.g., “Breast”. :information_source: : you can set parent to tissue, which is the reserved word to place the given cancer type at “root” level in the “studies oncotree” that will be generated in the homepage (aka query page) of the portal.
      
    implicit private val JsonWritesMeta = Json.writes[meta]        
    def format(x: meta): String =
      utils.Utils.formatJsValue(
        Json.toJson[meta](x))

    implicit private val JsonWritesData = Json.writes[data]        
    def format(x: data): String =
      utils.Utils.formatJsValue(
        Json.toJson[data](x))                 

  }
  