package pedcbimport.study

import enumeratum.EnumEntry
import enumeratum.Enum
import enumeratum.PlayJsonEnum
import utils.Implicits.StringImplicits
import pedcbimport.common.Wrapper._
import play.api.libs.json.Json

// ===========================================================================
case class Study(
    _id:            StudyId, // TODO: regex
  
    cancer_type:    CancerTypeId, // vs using CancerTypeCode?
   `rdfs:label`:    String,
   `rdfs:comment`:  String,
    short_name:     String,
    //public:         Boolean,
    pubmed_id:      Option[String], // TODO: regex
    citation:       Option[String], // TODO: regex; can there only ever be more than one?; TODO: should use an ID instead
    authorizations: Seq[Authorization]
    //adult:          Boolean
      )  {
  
      def toMeta =
        Study.meta(
          type_of_cancer          = cancer_type.format,
          cancer_study_identifier = _id.format,
          name                    = `rdfs:label`,
          description             = `rdfs:comment`,
          citation                = citation,
          pmid                    = pubmed_id,
          short_name              = short_name,
          groups                  = authorizations.map(_.entryName).mkString(";"),                  
          study_access            = "private", // TODO: always?
          add_global_case_list    = true,          
          is_adult_cancer         = false)
      
      def format: String =
        utils.Utils.formatJsValue(
          Json.toJson(toMeta)(Json.writes[Study.meta]))

      def writeMetaFile(parentDirPath: String) {
        s"${parentDirPath}/meta_study.txt"
          .path
          .writeFile(format)
      }  
  
}

  // ---------------------------------------------------------------------------
  object Study {
   
    def fromRawStudy(rawStudy: RawStudy): Study =
      Study(
        _id            = StudyId(rawStudy.CANCER_STUDY_IDENTIFIER),
    
        cancer_type    = CancerTypeId(rawStudy.TYPE_OF_CANCER_ID), // TODO; enforce part of enum or specified as new
       `rdfs:label`    = rawStudy.NAME,
       `rdfs:comment`  = rawStudy.DESCRIPTION,
        short_name     = rawStudy.SHORT_NAME,
        //public       = rawStudy.PUBLIC.asBooleanFromBit,
        pubmed_id      = rawStudy.PMID    .asOptionIfNULL,
        citation       = rawStudy.CITATION.asOptionIfNULL,
        authorizations = rawStudy.GROUPS.split(";").map(Authorization.withName).toVector
        //adult          = rawStudy.IS_ADULT.asBooleanFromBit.get
        )  
      
      import pedcbimport.common.CommonPlayJson._        
      def fromJson(json: String): Study =
        Json
          .fromJson[Study](
            Json.parse(json))
          .get

      // ---------------------------------------------------------------------------
      case class meta(
          type_of_cancer:          String,
          cancer_study_identifier: String,
          name:                    String,
          description:             String,
          citation:                Option[String],
          pmid:                    Option[String],
          short_name:              String,
          groups:                  String,
          
          add_global_case_list:    Boolean,          
          study_access:            String,
          is_adult_cancer:         Boolean) {
        
        implicit private val JsonWrites = Json.writes[meta]       
        
        def format(x: meta): String =
          utils.Utils.formatJsValue(
            Json.toJson[meta](x))

      }        
  }
      
// ===========================================================================    
sealed trait Authorization
    extends EnumEntry
  object Authorization
      extends Enum[Authorization]
         with PlayJsonEnum[Authorization] {
    val values = findValues

    case object `CBTTC`         extends Authorization
  case object `GROUPS`        extends Authorization
  case object `ICR`           extends Authorization
  case object `MARIS_LAB`     extends Authorization
  case object `NORTHCOTT_LAB` extends Authorization
  case object `PRIVATE`       extends Authorization
  case object `PUBLIC`        extends Authorization
  case object `SU2C`          extends Authorization
}

// ---------------------------------------------------------------------------
sealed trait CancerTypeCode
    extends EnumEntry
  @deprecated("people can define their own?") object CancerTypeCode
      extends Enum[CancerTypeCode]
         with PlayJsonEnum[CancerTypeCode] {
    val values = findValues

  case object `acc`			extends CancerTypeCode
  case object `acyc`			extends CancerTypeCode
  case object `all`			extends CancerTypeCode
  case object `aml`			extends CancerTypeCode
  case object `blca`			extends CancerTypeCode
  case object `brca`			extends CancerTypeCode
  case object `ccrcc`			extends CancerTypeCode
  case object `ccsk`			extends CancerTypeCode
  case object `cesc`			extends CancerTypeCode
  case object `chdm`			extends CancerTypeCode
  case object `chol`			extends CancerTypeCode
  case object `chrcc`			extends CancerTypeCode
  case object `coadread`	extends CancerTypeCode
  case object `cranio`		extends CancerTypeCode
  case object `difg`			extends CancerTypeCode
  case object `dipg`			extends CancerTypeCode
  case object `dlbcl`			extends CancerTypeCode
  case object `epm`			extends CancerTypeCode
  case object `es`			extends CancerTypeCode
  case object `esca`			extends CancerTypeCode
  case object `escc`			extends CancerTypeCode
  case object `ganglio`		extends CancerTypeCode
  case object `gbm`			extends CancerTypeCode
  case object `glioma`		extends CancerTypeCode
  case object `hcc`			extends CancerTypeCode
  case object `hgsoc`			extends CancerTypeCode
  case object `hnsc`			extends CancerTypeCode
  case object `luad`			extends CancerTypeCode
  case object `lusc`			extends CancerTypeCode
  case object `mbl`			extends CancerTypeCode
  case object `mixed`			extends CancerTypeCode
  case object `mm`			extends CancerTypeCode
  case object `mnet`			extends CancerTypeCode
  case object `mpnst`			extends CancerTypeCode
  case object `nbl`			extends CancerTypeCode
  case object `npc`			extends CancerTypeCode
  case object `paad`			extends CancerTypeCode
  case object `phgg`			extends CancerTypeCode
  case object `plgg`			extends CancerTypeCode
  case object `pnet`			extends CancerTypeCode
  case object `pptp`			extends CancerTypeCode
  case object `prad`			extends CancerTypeCode
  case object `prcc`			extends CancerTypeCode
  case object `scco`			extends CancerTypeCode
  case object `sclc`			extends CancerTypeCode
  case object `skcm`			extends CancerTypeCode
  case object `soft_tissue`	extends CancerTypeCode
  case object `stad`			extends CancerTypeCode
  case object `thpa`			extends CancerTypeCode
  case object `ucec`			extends CancerTypeCode
  case object `ucs`			extends CancerTypeCode
  case object `wt`			extends CancerTypeCode
}

// ===========================================================================
