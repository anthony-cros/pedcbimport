package test

import org.junit.Assert
import org.junit.Test
import pedcbimport.SanityCheck
import utils.Implicits.StringImplicits
import pedcbimport.OutputType

// ===========================================================================
class End2EndTest {

  /*
   * TODO:
   * - need to find real life matches in external files
   */
	@Test def testEndToEnd {

    val geneKvpsFilePath  = "end2end/input/external/genes.kvp" .path.fullPathFromTestResources
    val cosmicVcfFilePath = "end2end/input/external/cosmic.vcf".path.fullPathFromTestResources
    val mRnaTsvFilePath   = "end2end/input/external/mrna.tsv"  .path.fullPathFromTestResources
    val exonsBedFilePath  = "end2end/input/external/exons.bed" .path.fullPathFromTestResources
          
    val studyJsonUrl      = "end2end/input/data/studies.json"     .path.fullPathFromTestResources
    val cancerTypeJsonUrl = "end2end/input/data/cancer_types.json".path.fullPathFromTestResources

    val samplesDirPath    = "end2end/input/data/my_study".path.fullPathFromTestResources
    
    val referencesDirPath = "end2end/references".path.fullPathFromTestResources
		
    // ---------------------------------------------------------------------------
    Assert.assertEquals(None, SanityCheck(samplesDirPath))

    val actual: Map[OutputType, String] =        
      pedcbimport.PedCBImport(
        geneKvpsFilePath,
        cosmicVcfFilePath,             
        mRnaTsvFilePath,
        exonsBedFilePath,
        
        studyJsonUrl,
        cancerTypeJsonUrl,
        
        samplesDirPath)

    //s"/tmp/pedcbimport".path.mkdirs; actual.foreach { case (k, v) => s"/tmp/pedcbimport/${k}".path.parent.mkdirs; s"/tmp/pedcbimport/${k}".path.writeFile(v) }
        
    val references: List[String] =
      referencesDirPath
        .path
        .listFilesRecursively

    references
      .foreach { referenceFilePath =>
        val combo   = OutputType.withName(referenceFilePath.remove(referencesDirPath.suffixWithSlash))
        val content = referenceFilePath.path.readFile
        
        Assert.assertEquals( // TODO: keep trims?
          actual(combo).trim,
          content.trim)
      }

    Assert.assertEquals(
      actual.size,
      references.size)
  }
							  
}

// ===========================================================================
