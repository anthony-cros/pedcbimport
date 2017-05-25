package pedcbimport.meta

import pedcbimport.Enums
import pedcbimport.common.Wrapper._
import play.api.libs.json.Json
import enumeratum.{Enum, EnumEntry, PlayJsonEnum}
import utils.Implicits.StringImplicits

// ===========================================================================
object MetaMutation {
        
  sealed trait profile_description
      extends EnumEntry
    object profile_description
        extends Enum[profile_description]
           with PlayJsonEnum[profile_description] {
      val values = findValues

      case object `Exome genome sequencing mutation from TGEN.` extends profile_description // Translational Genomics Research Institute 
      case object `Mutation data from whole exome sequencing.`  extends profile_description
      case object `Mutation data from whole genome sequencing.` extends profile_description
        case object `Whole exome sequencing mutation.`          extends profile_description // same?
    }  
     
  // ===========================================================================
  def toMeta(study_id: StudyId, profile_description: profile_description) =
    meta(
      cancer_study_identifier      = study_id.format,
      genetic_alteration_type      = Enums.genetic_alteration_type.MUTATION_EXTENDED.entryName,
      datatype                     = Enums.datatype.MAF.entryName,
      stable_id                    = Enums.stable_id.mutations.entryName,
      show_profile_in_analysis_tab = true, // always for mutation; TODO: unless data file is empty
      profile_name                 = "Mutations", // TODO: always for mutations?
      profile_description          = profile_description.entryName,
      data_filename                = Enums.stable_id.mutations.entryName.surroundWith("data_", ".txt"),
      gene_panel                   = None,
      
      swissprot_identifier         = None)

  def toMetaContent(study_id: StudyId, profile_description: profile_description) =
    utils.Utils.formatJsValue(Json.toJson(toMeta(study_id, profile_description))(Json.writes[meta]))
      
  def writeMetaFile(parentDirPath: String, study_id: StudyId, profile_description: profile_description) = {
    s"${parentDirPath}/meta_${Enums.stable_id.mutations.entryName}.txt"
      .path
      .writeFile(
        toMetaContent(study_id, profile_description))
  }
  
  // ---------------------------------------------------------------------------
  case class meta(
      cancer_study_identifier:      String, //same value as specified in study meta file
      genetic_alteration_type:      String, //MUTATION_EXTENDED
      datatype:                     String, //MAF
      stable_id:                    String, //mutations
      show_profile_in_analysis_tab: Boolean, //true
      profile_name:                 String, //A name for the mutation data, e.g., “Mutations”.
      profile_description:          String, //A description of the mutation data, e.g., “Mutation data from whole exome sequencing.”.
      
      // seems to default to "data_mutations_extended.txt"
      data_filename:                String, //your data file

      gene_panel:                   Option[String], //optional gene panel stable id
      swissprot_identifier:         Option[String]) { //(optional) either accession or name, indicating the type of identifier in the SWISSPROT column
    
    implicit val JsonWrites = Json.writes[meta]
      
    def format: String =
      utils.Utils.formatJsValue(
        Json.toJson[meta](this))
        
  }
    
}

// ===========================================================================
