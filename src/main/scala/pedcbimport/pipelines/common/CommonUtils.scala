package pedcbimport.pipelines.common

import utils.Implicits.StringImplicits
import pedcbimport.DataType
import pedcbimport.pipelines.common.Wrapper.SampleId

// ===========================================================================
object CommonUtils {

  def mutationSampleIds(samplesDirPath: String) =
    sampleIds(
      samplesDirPath,
      CommonConstants.Extensions(DataType.Mutation))        

  def copyNumberSampleIds(samplesDirPath: String) =
    sampleIds(
      samplesDirPath,
      CommonConstants.Extensions(DataType.CopyNumber))        

  def expressionSampleIds(samplesDirPath: String) =
    sampleIds(
      samplesDirPath,
      CommonConstants.Extensions(DataType.Expression))        

  private def sampleIds(samplesDirPath: String, extension: String) =
    samplesDirPath
      .path
      .listPaths
      .filter(_.endsWith(extension))
      .map(_.path.basename)
      .map(SampleId.apply)
  
  // ---------------------------------------------------------------------------
  def mutationSampleFilePaths(samplesDirPath: String): List[String] =
    sampleFilePaths(samplesDirPath, CommonConstants.Extensions(DataType.Mutation))
    
  def copyNumberSampleFilePaths(samplesDirPath: String): List[String] =
    sampleFilePaths(samplesDirPath, CommonConstants.Extensions(DataType.CopyNumber))
    
  def expressionSampleFilePaths(samplesDirPath: String): List[String] =
    sampleFilePaths(samplesDirPath, CommonConstants.Extensions(DataType.Expression))
    
  private def sampleFilePaths(samplesDirPath: String, extension: String): List[String] =
    samplesDirPath
      .path
      .listPaths
      .filter(
        _.endsWith(extension))
          
}

// ===========================================================================
