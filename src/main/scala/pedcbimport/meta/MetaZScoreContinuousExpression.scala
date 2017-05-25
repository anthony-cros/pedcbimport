package pedcbimport.meta

import play.api.libs.json.Json
import pedcbimport.common.Wrapper._
import pedcbimport.Enums
import enumeratum.{Enum, EnumEntry, PlayJsonEnum}
import utils.Implicits.StringImplicits

// ===========================================================================
object MetaZScoreContinuousExpression {
  
    def toMeta(study_id: StudyId) =
      meta(
        cancer_study_identifier      = study_id.format,
        genetic_alteration_type      = Enums.genetic_alteration_type.MRNA_EXPRESSION.entryName,
        datatype                     = Enums.datatype.`Z-SCORE`.entryName,
        stable_id                    = Enums.stable_id.rna_seq_mrna_median_Zscores.entryName, // TODO: median ok? original is rna_seq_zScore or rna_seq_mrna_zScore; vs "expression" for the median ones
        show_profile_in_analysis_tab = true, // always
        profile_name                 = "mRNA Expression z-Scores (RNA-Seq)", // vs "mRNA expression" for the _medians ones...         
        profile_description          = "mRNA expression (Z-Scores)", // "Expression levels." for the _medians ones...
        data_filename                = Enums.stable_id.rna_seq_mrna_median_Zscores.entryName.surroundWith("data_", ".txt"),
        gene_panel                   = None,
        normals_reference_mapping_id = "gtex")

    def toMetaContent(study_id: StudyId) =
      utils.Utils.formatJsValue(Json.toJson(toMeta(study_id))(Json.writes[meta]))
        
    def writeMetaFile(parentDirPath: String, study_id: StudyId) = {
      s"${parentDirPath}/meta_${Enums.stable_id.rna_seq_mrna_median_Zscores.entryName}.txt"
        .path
        .writeFile(
          toMetaContent(study_id))
    }
    
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
  
        gene_panel:                   Option[String],
            
        normals_reference_mapping_id: String) { //(optional) either accession or name, indicating the type of identifier in the SWISSPROT column

      implicit val JsonWrites = Json.writes[meta]
      
      def format: String =
        utils.Utils.formatJsValue(
          Json.toJson[meta](this)) 
    }
    
}

// ===========================================================================