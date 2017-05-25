package pedcbimport.pipelines.copynumber

// ===========================================================================
object CopyNumberData {
  
  def apply(
        geneKvpsFilePath:  String,
  
        mRnaTsvFilePath:   String,    
        exonsBedFilePath:  String,    
        
        samplesDirPath: String)
      : utils.Tsv = 
    CopyNumberPhase2( 
      CopyNumberPhase1(
        geneKvpsFilePath,
  
        mRnaTsvFilePath,    
        exonsBedFilePath,    
          
        samplesDirPath))
  
}

// ===========================================================================
