package pedcbimport.pipelines.copynumber

import utils.Implicits.StringImplicits
import utils.Implicits.IterableImplicits
import utils.Implicits.PairsSeqImplicits
import pedcbimport.common._
import pedcbimport.common.CommonUtils
import pedcbimport.common.Wrapper._
import pedcbimport.references._
import utils.Overlap
import utils.Overlap._

// ===========================================================================
/* this is a port of https://github.com/d3b-center/d3b-maf2pedcbio/blob/f0806f2/convert-to-cbio-wgs-somatic/anno_cnv.pl */
object CopyNumberPhase1 {
    
  /**
    example output:
    <pre>
      7316-9222T_DNA-Tumor    chr20   0               9561528         C20orf27        NA      	1       full
      7316-9222T_DNA-Tumor    chr3    31461513        48394926        PRSS45          377047  	1       full
      7316-9222T_DNA-Tumor    chr10   89647383        124343235       MIR1287         100302133 -1      full
      7316-9222T_DNA-Tumor    chr16   46500348        90172812        WWP2            11060   	-1      full
      7316-9222T_DNA-Tumor    chr10   89431713        89647383        PTEN            5728    	-2      part
      7316-9222T_DNA-Tumor    chr10   89647383        124343235       DMBT1           1755    	-1      part
      ...
		</pre>
  */
  def apply(
        geneKvpsFilePath:  String,
  
        mRnaTsvFilePath:   String,    
        exonsBedFilePath:  String,    

        samplesDirPath: String)
      : Iterable[Result] = {              

    CommonUtils.copyNumberSampleFilePaths(samplesDirPath)
      .flatMap { rawSampleOriginCopyNumberFilePath =>  
        copyNumbers(
              rawSampleOriginCopyNumberFilePath
                .path
                .streamTsvFile())     
           .flatMap(
              (results(
                   geneLookup = GeneLookup(geneKvpsFilePath),
                   mRNAs      = mRNAs(mRnaTsvFilePath),
                   exons      = Exons(exonsBedFilePath),
                   sampleId = SampleId(rawSampleOriginCopyNumberFilePath.path.basename))
                   _).tupled) }
       .filterNot(_.overlapResult.isDiscarded)
  }
      
  // ---------------------------------------------------------------------------
  private def copyNumbers(rows: Iterable[Vector[String]]): Map[Position, CopyNumberType] =
    rows
       .map(_.toIterator)
       .map(RawCopyNumber.apply)
       .map { rawCopyNumber =>
         val position = 
           Position(
             Chromosome(rawCopyNumber.chromosome_number),
             ChromosomePosition(
               rawCopyNumber.start.toLong,
               rawCopyNumber.end  .toLong))

         val tipe = 
           rawCopyNumber.count.toInt match {
             case 0 => CopyNumberType.`-2`
             case 1 => CopyNumberType.`-1`
             case 2 => CopyNumberType. `0`
             case 3 => CopyNumberType. `1`
             case count if count > 3 => CopyNumberType. `2`
           }
               
         position -> tipe
       }
       .toMap       
             
   // ---------------------------------------------------------------------------
   private def results(       
         geneLookup: Map[HugoGeneSymbol, EntrezGeneId],
         mRNAs:      Map[Chromosome, Map[ChromosomePosition, HugoGeneSymbol]],
         exons:      Map[Chromosome, Map[ChromosomePosition, HugoGeneSymbol]],
         
         sampleId:           SampleId)(
         copyNumberPosition: Position,
         copyNumberType:     CopyNumberType)
       : Iterable[Result] = {
     
     // TODO: double check the inclusions + edge cases (like overlap by one)
     val overlapResults = 
       CopyNumberOverlap(
         copyNumberPosition.position,
         mRNAs(copyNumberPosition.chromosome),
         exons(copyNumberPosition.chromosome))
     
     overlapResults
       .map { case (overlapResult, hugoGeneSymbol) => // TODO: use an intermediate that groups by
         val gene =           
          Gene(
            hugoGeneSymbol     = hugoGeneSymbol,
            entrezGeneIdOpt    = geneLookup.get(hugoGeneSymbol))

         Result(
            sampleId           = sampleId,
            copyNumberPosition = copyNumberPosition,
            gene               = gene,
            copyNumberType     = copyNumberType,
            overlapResult      = overlapResult) }
  }


}

// ===========================================================================
