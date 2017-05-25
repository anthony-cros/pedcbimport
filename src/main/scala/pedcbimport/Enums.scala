package pedcbimport

import enumeratum.EnumEntry
import enumeratum.Enum
import enumeratum.PlayJsonEnum

// ===========================================================================
object Enums {

  sealed trait NullValue
      extends EnumEntry {
    
      val format: String
    
    }
    object NullValue
        extends Enum[NullValue]
           with PlayJsonEnum[NullValue] {
      val values = findValues
  
      // MRNA_EXPRESSION
      case object `could_not_be_measured_or_detected` extends NullValue { override val format = "NA" }
      case object `was_not_measured_or_detected`      extends NullValue { override val format = "NA" }        
    }
  
  // ===========================================================================
  sealed trait genetic_alteration_type
      extends EnumEntry
    object genetic_alteration_type
        extends Enum[genetic_alteration_type]
           with PlayJsonEnum[genetic_alteration_type] {
      val values = findValues
  
      case object `CANCER_TYPE`            extends genetic_alteration_type
      
      case object `CLINICAL`               extends genetic_alteration_type

      case object `COPY_NUMBER_ALTERATION` extends genetic_alteration_type      
      case object `MRNA_EXPRESSION`        extends genetic_alteration_type      
      case object `MUTATION_EXTENDED`      extends genetic_alteration_type      
      case object `METHYLATION`            extends genetic_alteration_type
      case object `PROTEIN_LEVEL`          extends genetic_alteration_type
      case object `FUSION`                 extends genetic_alteration_type
      
      case object `GISTIC_GENES_AMP`       extends genetic_alteration_type
      case object `GISTIC_GENES_DEL`       extends genetic_alteration_type
      
      case object `MUTSIG`       extends genetic_alteration_type
    }

  // ---------------------------------------------------------------------------
  sealed trait cna
  sealed trait cna_discrete
  
  // ---------------------------------------------------------------------------
  sealed trait datatype
      extends EnumEntry
    object datatype
        extends Enum[datatype]
           with PlayJsonEnum[datatype] {
      val values = findValues
  
      case object `CANCER_TYPE` extends datatype
      
      case object `PATIENT_ATTRIBUTES` extends datatype
      case object `SAMPLE_ATTRIBUTES`  extends datatype
      case object `TIMELINE`           extends datatype
      
      // COPY_NUMBER_ALTERATION
      case object `DISCRETE`   extends datatype with cna
      case object `CONTINUOUS` extends datatype with cna
      case object `LOG2-VALUE` extends datatype with cna
      case object `SEG`        extends datatype with cna
      
      // MRNA_EXPRESSION
    //case object `DISCRETE`   extends datatype
    //case object `CONTINUOUS` extends datatype
      case object `Z-SCORE` extends datatype
      
      // MUTATION_EXTENDED
      case object `MAF` extends datatype
      
      // METHYLATION
    //case object `CONTINUOUS` extends datatype
      
      // PROTEIN_LEVEL
    //case object `LOG2-VALUE` extends datatype
    //case object `Z-SCORE`    extends datatype

      // FUSION
      case object `FUSION` extends datatype
      
      // GISTIC_GENES_AMP/GISTIC_GENES_DEL & MUTSIG
      case object `Q-VALUE` extends datatype
      
    }

  // ---------------------------------------------------------------------------
  // https://github.com/cBioPortal/cbioportal/blob/master/core/src/main/scripts/importer/allowed_data_types.txt
  sealed trait stable_id
      extends EnumEntry
    object stable_id
        extends Enum[stable_id]
           with PlayJsonEnum[stable_id] {
      val values = findValues
  
      // DISCRETE
      case object `gistic`        extends stable_id with cna_discrete
      case object `cna`           extends stable_id with cna_discrete
      case object `cna_rae`       extends stable_id with cna_discrete
      case object `cna_consensus` extends stable_id with cna_discrete
      
      // CONTINUOUS
      case object `linear_CNA` extends stable_id
      
      // LOG2-VALUE
      case object `log2CNA` extends stable_id
      
      // SEG
      // no stable_id? 
      
      // MRNA_EXPRESSION
      // ...      
      case object `mrna_U133` extends stable_id
      case object `mrna_U133_Zscores` extends stable_id
      case object `rna_seq_mrna_median_Zscores` extends stable_id
      case object `mrna_median_Zscores` extends stable_id
      case object `rna_seq_mrna` extends stable_id
      case object `rna_seq_v2_mrna` extends stable_id
      case object `rna_seq_v2_mrna_median_Zscores` extends stable_id
      case object `mirna` extends stable_id
      case object `mirna_median_Zscores` extends stable_id
      case object `mrna_merged_median_Zscores` extends stable_id
      case object `mrna` extends stable_id
      case object `mrna_outliers` extends stable_id
      case object `mrna_zbynorm` extends stable_id
      case object `rna_seq_mrna_capture` extends stable_id
      case object `rna_seq_mrna_capture_Zscores` extends stable_id
      
      // MUTATION_EXTENDED
      case object `mutations` extends stable_id
      
      // METHYLATION; depends on platform
      case object `methylation_hm27`  extends stable_id
      case object `methylation_hm450` extends stable_id
      
      // PROTEIN_LEVEL
      case object `rppa` extends stable_id
      case object `rppa_Zscores` extends stable_id // for Oncoprint UI
      
      // FUSION
    //case object `mutations` extends datatype

    }  
  
  // ===========================================================================
  sealed trait OS_STATUS
      extends EnumEntry
    object OS_STATUS
        extends Enum[OS_STATUS]
           with PlayJsonEnum[OS_STATUS] {
      val values = findValues
        
      case object `DECEASED` extends OS_STATUS
      case object `LIVING`   extends OS_STATUS
    }  

  sealed trait DFS_STATUS
      extends EnumEntry
    object DFS_STATUS
        extends Enum[DFS_STATUS]
           with PlayJsonEnum[DFS_STATUS] {
      val values = findValues
        
      case object `DiseaseFree` extends DFS_STATUS
      case object `Recurred/Progressed`   extends DFS_STATUS
    }  

  // ===========================================================================
  sealed trait SAMPLE_TYPE
      extends EnumEntry
    object SAMPLE_TYPE
        extends Enum[SAMPLE_TYPE]
           with PlayJsonEnum[SAMPLE_TYPE] {
      val values = findValues
        
      case object `recurrence` extends SAMPLE_TYPE
      case object `metastatic` extends SAMPLE_TYPE
      case object `primary`    extends SAMPLE_TYPE
    }
  
  // ===========================================================================
  sealed trait swissprot_identifier
      extends EnumEntry
    object swissprot_identifier
        extends Enum[swissprot_identifier]
           with PlayJsonEnum[swissprot_identifier] {
      val values = findValues
        
      case object `accession` extends swissprot_identifier
      case object `name`      extends swissprot_identifier
    }  

  // ===========================================================================
  // cna
  sealed trait cna_discrete_stable_id
      extends EnumEntry {
    
      val as_stable_id: stable_id =
        stable_id.withName(entryName)
      
    }
    object cna_discrete_stable_id
        extends Enum[cna_discrete_stable_id]
           with PlayJsonEnum[cna_discrete_stable_id] {
      val values = findValues
      
      case object `gistic`           extends cna_discrete_stable_id
      case object `cna`              extends cna_discrete_stable_id
      case object `cna_rae`          extends cna_discrete_stable_id
      case object `cnacna_consensus` extends cna_discrete_stable_id
    }  
  
  // ===========================================================================
  // fusion
  
  sealed trait Frame
      extends EnumEntry
    object Frame
        extends Enum[Frame]
           with PlayJsonEnum[Frame] {
      val values = findValues
        
      case object `in-frame`   extends Frame
      case object `frameshift` extends Frame
    }  
  
  sealed trait YesNo
      extends EnumEntry
    object YesNo
        extends Enum[YesNo]
           with PlayJsonEnum[YesNo] {
      val values = findValues
        
      case object `yes` extends YesNo
      case object `no`  extends YesNo
    }  
  
  // ===========================================================================
  // case lists
 
  sealed trait case_list_suffix
      extends EnumEntry
    object case_list_suffix
        extends Enum[case_list_suffix]
           with PlayJsonEnum[case_list_suffix] {
      val values = findValues
      
      case object `_all` extends case_list_suffix
      
      case object `_sequenced`         extends case_list_suffix
      case object `_cna`               extends case_list_suffix
      case object `_rna_seq_v2_mrna`   extends case_list_suffix
      case object `_mrna`              extends case_list_suffix
      case object `_methylation_hm27`  extends case_list_suffix
      case object `_rppa`              extends case_list_suffix
      case object `_3way_complete`     extends case_list_suffix // mRNA, CNA & sequencing

    }  
  
}

// ===========================================================================
