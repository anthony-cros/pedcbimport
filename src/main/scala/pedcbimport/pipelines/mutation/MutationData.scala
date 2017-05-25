package pedcbimport.pipelines.mutation

import utils.Implicits.ProductImplicits
import utils.Implicits.StringImplicits
import utils.Implicits.SeqImplicits
import utils.Implicits.IterableImplicits
import utils.Implicits.OptionImplicits
import pedcbimport.pipelines.common.Wrapper._
import pedcbimport.pipelines.common.CommonUtils
import pedcbimport.references.CosmicLookup
import pedcbimport.references.GeneLookup
import utils.Tsv

// ===========================================================================
object MutationData {

  def apply(
		  geneKvpsFilePath:  String,		  
      cosmicVcfFilePath: String,      
      samplesDirPath:    String)
    : Tsv = {
    
    val cosmicLookup: CosmicLookup =
      CosmicLookup(cosmicVcfFilePath) 
        
    val geneLookup: Map[HugoGeneSymbol, EntrezGeneId] =
      GeneLookup(geneKvpsFilePath)    
      
    // ---------------------------------------------------------------------------
    val originalSampleMafFilePaths: List[String] =
      CommonUtils.mutationSampleFilePaths(samplesDirPath)

    // ---------------------------------------------------------------------------
    val rawSampleMafDataRows: Iterable[Vector[String]] =
      originalSampleMafFilePaths
        .flatMap(
          _
            .path
            .streamTsvFile()
            .dropExpected(Vector(MutationConstants.FirstLine))
              .force
            .dropHeader)
        
    apply(
      cosmicLookup,
      geneLookup,
      rawSampleMafDataRows)     
  }
  
  // ===========================================================================
  /* see original logic at https://github.com/d3b-center/d3b-maf2pedcbio/blob/378e61c/convert-to-cbio-wes-somatic/trans_to_cbio_wes-somatic.pl */
  private def apply(
        cosmicLookup:         CosmicLookup,
        geneLookup:           Map[HugoGeneSymbol, EntrezGeneId],
        rawSampleMafDataRows: Iterable[Vector[String]])
      : Tsv = { 
    
    // convert original sample MAF rows into target ones (mostly filtering rows/columns, lookups and misc value manipulations)
    val targetSampleRows: Iterable[SampleRow] =
      rawSampleMafDataRows
        .map(_.iterator)
        .map(RawSampleMafRow.apply)
        
        // filter out any row whose variant type matches one of the discarded variant types
        .filterNot(rawSampleMafRow =>
          MutationConstants.DiscardedVariantType.values.map(_.entryName).exists(
            rawSampleMafRow.Variant_Type.matches))
            
        // filter out any row whose hugo symbol is unknown
        .filterNot(rawSampleMafRow =>
          rawSampleMafRow.Hugo_Symbol.format.matches(
            MutationConstants.UnknownHugoSymbolValue))
        
        // remove extraneous sample barcode suffices; TODO: remove from pipeline itself?
        .map { rawSampleMafRow =>          
          rawSampleMafRow.copy(          
            Tumor_Sample_Barcode        = rawSampleMafRow.Tumor_Sample_Barcode       .replaceAll("-DNA-Tumor$",  ""),
            Matched_Norm_Sample_Barcode = rawSampleMafRow.Matched_Norm_Sample_Barcode.replaceAll("-DNA-Normal$", "")) 
        }
        
        // transform row
        .map { rawSampleMafRow =>          
          SampleRow(
            Hugo_Symbol                  = rawSampleMafRow.Hugo_Symbol,
            Entrez_Gene_Id               = geneLookup.get(rawSampleMafRow.Hugo_Symbol),
            
            Center                       = rawSampleMafRow.Center,
            NCBI_Build                   = rawSampleMafRow.NCBI_Build,
            Chromosome                   = rawSampleMafRow.Chromosome,
            Start_Position               = rawSampleMafRow.Start_Position,
            End_Position                 = rawSampleMafRow.End_Position,
            Strand                       = rawSampleMafRow.Strand,
            Variant_Classification       = rawSampleMafRow.Variant_Classification,
            Variant_Type                 = rawSampleMafRow.Variant_Type,
            Reference_Allele             = rawSampleMafRow.Reference_Allele,
            Tumor_Seq_Allele1            = rawSampleMafRow.Tumor_Seq_Allele1,
            Tumor_Seq_Allele2            = rawSampleMafRow.Tumor_Seq_Allele2,
            dbSNP_RS                     = rawSampleMafRow.dbSNP_RS,
            dbSNP_Val_Status             = rawSampleMafRow.dbSNP_Val_Status,
            Tumor_Sample_Barcode         = rawSampleMafRow.Tumor_Sample_Barcode,
            Matched_Norm_Sample_Barcode  = rawSampleMafRow.Matched_Norm_Sample_Barcode,
            Match_Norm_Seq_Allele1       = rawSampleMafRow.Match_Norm_Seq_Allele1,
            Match_Norm_Seq_Allele2       = rawSampleMafRow.Match_Norm_Seq_Allele2,
            
            Amino_Acid_Change            = rawSampleMafRow.Amino_acids, // TODO: wrong index? why is ours called differently?
            
            ONCOTATOR_COSMIC_OVERLAPPING =
              cosmicLookup
                .rowOpt(
                   rawSampleMafRow.Chromosome,
                   rawSampleMafRow.Start_Position) ) }

     val header: Vector[String] =
       classOf[SampleRow].getDeclaredFields.map(_.getName).toVector

     val data: Iterable[Vector[String]] =
       targetSampleRows.map(_.formatVector)
     
     Tsv(
       Seq(header) ++
       data)
  }
  
}

// ===========================================================================
