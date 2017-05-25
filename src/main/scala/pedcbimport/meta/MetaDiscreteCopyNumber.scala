package pedcbimport.meta

import pedcbimport.Enums
import pedcbimport.pipelines.common.Wrapper._
import play.api.libs.json.Json
import enumeratum.{Enum, EnumEntry, PlayJsonEnum}
import utils.Implicits.StringImplicits

// ===========================================================================       
object MetaDiscreteCopyNumber {
  
  def toMeta(study_id: StudyId) =
    meta(
      cancer_study_identifier      = study_id.format,
      genetic_alteration_type      = Enums.genetic_alteration_type.COPY_NUMBER_ALTERATION.entryName,
      datatype                     = Enums.datatype.DISCRETE.entryName,
      stable_id                    = Enums.stable_id.cna.entryName,
      show_profile_in_analysis_tab = true, // TODO
      profile_name                 = "Putative copy-number alterations from GISTIC",
      profile_description          = "Putative copy-number from GISTIC 2.0. Values: -2 = homozygous deletion; -1 = hemizygous deletion; 0 = neutral / no change; 1 = gain; 2 = high level amplification.",
      data_filename                = Enums.stable_id.cna.entryName.surroundWith("data_", ".txt"),
      gene_panel                   = None)

  def toMetaContent(study_id: StudyId) =
    utils.Utils.formatJsValue(Json.toJson(toMeta(study_id))(Json.writes[meta]))
      
  def writeMetaFile(parentDirPath: String, study_id: StudyId) =
    s"${parentDirPath}/meta_${Enums.stable_id.cna.entryName}.txt"
      .path
      .writeFile(
        toMetaContent(study_id))  
   
  // ---------------------------------------------------------------------------
  case class meta(
      cancer_study_identifier:      String, //same value as specified in study meta file
      genetic_alteration_type:      String, //MUTATION_EXTENDED
      datatype:                     String, //MAF
      stable_id:                    String, //Enums.cna_discrete,
      show_profile_in_analysis_tab: Boolean, //true
      profile_name:                 String, //A name for the mutation data, e.g., “Mutations”.
      profile_description:          String, //A description of the mutation data, e.g., “Mutation data from whole exome sequencing.”.
      
      // seems to default to "data_mutations_extended.txt"
      data_filename:                String, //your data file

      gene_panel:                   Option[String]) { //(optional) either accession or name, indicating the type of identifier in the SWISSPROT column

    implicit val JsonWrites = Json.writes[meta]
  
    def format: String =
      utils.Utils.formatJsValue(
          Json.toJson[meta](this))
  }

}    

// ===========================================================================
