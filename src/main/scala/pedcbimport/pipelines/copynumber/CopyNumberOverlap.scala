package pedcbimport.pipelines.copynumber

import pedcbimport.common.Wrapper._
import OverlapResult._
import utils.Overlap
import utils.Overlap._

// ===========================================================================
object CopyNumberOverlap {
    
  def apply(
        copyNumberPosition: ChromosomePosition,
        mRnas: Map[ChromosomePosition, HugoGeneSymbol],
        exons: Map[ChromosomePosition, HugoGeneSymbol])
      : Iterable[(OverlapResult, HugoGeneSymbol)] = // may return an empty list
    
    mRnas // examine overlap with mRNA
      .flatMap { case (mRnaPosition, hugoGeneSymbol) =>
        
        // get overlap with mRNA segment
        val overlapWithMRna: Overlap =
           copyNumberPosition
             .overlapWith(mRnaPosition)
        
        // determine action(s)
        overlapResultsMRna(
            copyNumberPosition,
            overlapWithMRna)
          match {            
            case Some(action) =>
              Iterable((action, hugoGeneSymbol)) // only one action

            case None =>       // need to examine at the exon level

              exons
                .map { case (exonPosition, hugoGeneSymbol) =>
                  
                // get overlap with exon segment                  
                 val overlapWithExon: Overlap =
                   copyNumberPosition
                     .overlapWith(exonPosition)
                  
                  // determine action
                  (overlapResultsExon(
                     copyNumberPosition,
                     overlapWithExon),
                   hugoGeneSymbol)} } }
  

  // ---------------------------------------------------------------------------
  private def overlapResultsMRna(
        copyNumberPosition: ChromosomePosition,
        overlapWithMRna:    Overlap)
      : Option[OverlapResult] =
        
     overlapWithMRna match {
       
       case StrictlyPrecedes  | StrictlySucceeds =>
         Some(Discarded)
         
       case PartiallyPrecedes | PartiallySucceeds =>
         Some(Part)
         
       case StrictlyContains  | StrictlyMatches =>
         Some(Full)
         
       case StrictlyContainedBy =>
         None // need to look at exons
     }  
  
  // ---------------------------------------------------------------------------
  private def overlapResultsExon(
        copyNumberPosition: ChromosomePosition,
        overlapWithExon:    Overlap)
      : OverlapResult =

     overlapWithExon match {
       
       case StrictlyPrecedes | StrictlySucceeds =>
         Discarded // TODO: this seems odd, seems to discard parts that would be included based on the mRNA overlap...
         
       case _ =>
         Part

     }  
    
}

// ===========================================================================
