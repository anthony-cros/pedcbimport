package pedcbimport

import enumeratum.{Enum, EnumEntry, PlayJsonEnum}

// ===========================================================================
sealed trait OutputType
    extends EnumEntry
  object OutputType
      extends Enum[OutputType]
         with PlayJsonEnum[OutputType] {
    val values = findValues
      
    case object `case_lists/cases_all.txt`        extends OutputType
    case object `case_lists/cases_mutation.txt`   extends OutputType
    case object `case_lists/cases_expression.txt` extends OutputType
    case object `case_lists/cases_copynumber.txt` extends OutputType

    case object `meta_study.txt` extends OutputType

    case object `meta_cancer_type.txt` extends OutputType
    case object `data_cancer_type.txt` extends OutputType

    case object `meta_clinical_sample.txt` extends OutputType
    case object `data_clinical_sample.txt` extends OutputType

    case object `meta_mutations.txt` extends OutputType
    case object `data_mutations.txt` extends OutputType

    case object `meta_copynumber.txt` extends OutputType
    case object `data_copynumber.txt` extends OutputType

    case object `meta_expression_raw.txt` extends OutputType
    case object `data_expression_raw.txt` extends OutputType

    case object `meta_expression_zscore.txt` extends OutputType
    case object `data_expression_zscore.txt` extends OutputType
  }


// ===========================================================================
