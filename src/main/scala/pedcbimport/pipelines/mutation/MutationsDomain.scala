package pedcbimport.pipelines.mutation

import pedcbimport.pipelines.common.Wrapper._
import pedcbimport.references.CosmicVcfRow

// ===========================================================================
case class SampleRow(
    Hugo_Symbol:                  HugoGeneSymbol,
    Entrez_Gene_Id:               Option[EntrezGeneId],
    Center:                       String,
    NCBI_Build:                   String,
    Chromosome:                   Chromosome,
    Start_Position:               StartPosition,
    End_Position:                 String,
    Strand:                       String,
    Variant_Classification:       String,
    Variant_Type:                 String,
    Reference_Allele:             String,
    Tumor_Seq_Allele1:            String,
    Tumor_Seq_Allele2:            String,
    dbSNP_RS:                     String,
    dbSNP_Val_Status:             String,
    Tumor_Sample_Barcode:         String,
    Matched_Norm_Sample_Barcode:  String,
    Match_Norm_Seq_Allele1:       String,
    Match_Norm_Seq_Allele2:       String,
    Amino_Acid_Change:            String,
    ONCOTATOR_COSMIC_OVERLAPPING: Option[CosmicVcfRow]) {    

  def formatVector: Vector[String] =
    Vector(
      Hugo_Symbol.format,
      Entrez_Gene_Id
        .map(_.format)
        .getOrElse(
          MutationConstants.MissingEntrezGeneIdReplacementValue),
      Center,
      NCBI_Build,
      Chromosome    .format,
      Start_Position.format,
      End_Position,
      Strand,
      Variant_Classification,
      Variant_Type,
      Reference_Allele,
      Tumor_Seq_Allele1,
      Tumor_Seq_Allele2,
      dbSNP_RS,
      dbSNP_Val_Status,
      Tumor_Sample_Barcode,
      Matched_Norm_Sample_Barcode,
      Match_Norm_Seq_Allele1,
      Match_Norm_Seq_Allele2,
      Amino_Acid_Change,         
      ONCOTATOR_COSMIC_OVERLAPPING
        .map(cosmicEntry =>
          s"${cosmicEntry.INFO.AA}(${cosmicEntry.INFO.CNT})")
        .getOrElse(
          MutationConstants.MissingCosmicReplacementValue) )

}

// ===========================================================================
