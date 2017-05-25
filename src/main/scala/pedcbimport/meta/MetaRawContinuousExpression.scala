package pedcbimport.meta

import pedcbimport.Enums
import pedcbimport.pipelines.common.Wrapper._
import play.api.libs.json.Json
import enumeratum.{Enum, EnumEntry, PlayJsonEnum}
import utils.Implicits.StringImplicits

// ===========================================================================
object MetaRawContinuousExpression {
  
    def toMeta(study_id: StudyId) =
      meta(
        cancer_study_identifier      = study_id.format,
        genetic_alteration_type      = Enums.genetic_alteration_type.MRNA_EXPRESSION.entryName,
        datatype                     = Enums.datatype.CONTINUOUS.entryName,
        stable_id                    = Enums.stable_id.rna_seq_mrna.entryName,
        show_profile_in_analysis_tab = false, // always, only true for z-score
        profile_name                 = "mRNA expression (RNA-seq FPKM)",
        profile_description          = "mRNA expression (RNA-seq FPKM)",
        data_filename                = Enums.stable_id.rna_seq_mrna.entryName.surroundWith("data_", ".txt"),
        gene_panel                   = None,
        normals_reference_mapping_id = "gtex") 

    def toMetaContent(study_id: StudyId) =
      utils.Utils.formatJsValue(Json.toJson(toMeta(study_id))(Json.writes[meta]))
        
    def writeMetaFile(parentDirPath: String, study_id: StudyId) = {
      s"${parentDirPath}/meta_${Enums.stable_id.rna_seq_mrna.entryName}.txt"
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