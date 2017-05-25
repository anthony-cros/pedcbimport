package pedcbimport

import utils.Implicits.AnythingImplicits
import utils.Implicits.IterableImplicits
import utils.Implicits.StringImplicits
import scala.collection.mutable
import play.api.libs.json.Json
import pedcbimport.meta._
import pedcbimport.meta.MetaMutation.profile_description
import pedcbimport.study.Study    
import pedcbimport.cancertype.CancerType
import pedcbimport.pipelines.common.Wrapper._
import pedcbimport.clinical.DataSample
import pedcbimport.pipelines.common._
import pedcbimport.pipelines.mutation._
import pedcbimport.pipelines.copynumber.CopyNumberData
import pedcbimport.pipelines.expression.ExpressionData
import pedcbimport.pipelines.common.CommonPlayJson._
import utils.Tsv
import OutputType._

// ===========================================================================
object PedCBImport {
  
  def main(args: Array[String]): Unit = {
		/* see end-to-end test for example input */    
  }
  
  def apply(
      
      // reference files
  		geneKvpsFilePath  : String,		
      cosmicVcfFilePath:  String,    
      mRnaTsvFilePath:    String,
      exonsBedFilePath:   String, 

      // "meta" data
      studyJsonUrl      : String,
      cancerTypeJsonUrl : String,
      
      // actual data
      samplesDirPath    : String)
    : Map[OutputType, String] = {
    
    val results = mutable.Map[OutputType, String]()
    
    val studyId: StudyId =
      samplesDirPath.path
        .basename
        .then(StudyId.apply)     
    
    val study: Study =
      studyJsonUrl
        .path
        .readFile()
        .then(Json.parse)
        .then(Json.fromJson[Seq[Study]])
        .get
        .find(_._id == studyId)
        .get

    val cancerTypeOpt: Option[CancerType] =
      cancerTypeJsonUrl
        .path
        .readFile()
        .then(Json.parse)
        .then(Json.fromJson[Seq[CancerType]])
        .get
        .find(_.study == studyId)

    val mutationSampleIds: Seq[SampleId] =
      CommonUtils.mutationSampleIds(samplesDirPath)     

    val copyNumberSampleIds: Seq[SampleId] =
      CommonUtils.copyNumberSampleIds(samplesDirPath)     

    val expressionSampleIds: Seq[SampleId] =
      CommonUtils.expressionSampleIds(samplesDirPath)           

    val sampleIds: Seq[SampleId] =
      (mutationSampleIds ++
       copyNumberSampleIds ++
       expressionSampleIds)
       .distinct           
      
    val caseListAll: CaseList =
      CaseList(
        studyId,
        samples = sampleIds.sortBy(_.format),
        Enums.case_list_suffix._all)

    val sampleMeta: MetaSample.meta =
      MetaSample.toMeta(studyId)
      
    val sampleData: Tsv =
      DataSample(sampleIds)


    // ===========================================================================      
    if (mutationSampleIds.nonEmpty) {        
      val caseList: CaseList =
        CaseList(
          studyId,
          mutationSampleIds,
          Enums.case_list_suffix._sequenced)
          
      val data: Tsv =
        MutationData(
          geneKvpsFilePath,
          cosmicVcfFilePath,
          samplesDirPath)
  
      val meta: MetaMutation.meta =
        MetaMutation
          .toMeta(
            studyId,
            profile_description.`Mutation data from whole genome sequencing.`)

      results(`case_lists/cases_mutation.txt`) = caseList.format.println
      results(`meta_mutations.txt`)  = meta.format.println
      results(`data_mutations.txt`)  = data.format.println 
    }

    // ---------------------------------------------------------------------------    
    if (copyNumberSampleIds.nonEmpty) {        
      val caseList: CaseList =
        CaseList(
          studyId,
          copyNumberSampleIds,
          Enums.case_list_suffix._cna)

      val data: Tsv =
        CopyNumberData(
          geneKvpsFilePath,
    
          mRnaTsvFilePath,    
          exonsBedFilePath,    
          
          samplesDirPath)

      val meta: MetaDiscreteCopyNumber.meta =
        MetaDiscreteCopyNumber.toMeta(studyId)            

      results(`case_lists/cases_copynumber.txt`) = caseList.format.println
      results(`meta_copynumber.txt`)  = meta.format.println
      results(`data_copynumber.txt`)  = data.format.println
    }
    
    // ---------------------------------------------------------------------------    
    if (expressionSampleIds.nonEmpty) {        
      val caseList: CaseList =
        CaseList(
          studyId,
          expressionSampleIds,
          Enums.case_list_suffix._mrna)
  
      val (data1, data2) =
        ExpressionData(
          geneKvpsFilePath,        
          samplesDirPath)
  
      val meta1: MetaRawContinuousExpression.meta =
        MetaRawContinuousExpression.toMeta(studyId)            

      val meta2: MetaZScoreContinuousExpression.meta =
        MetaZScoreContinuousExpression.toMeta(studyId)

      results(`case_lists/cases_expression.txt`) = caseList.format.println

      results(`meta_expression_raw.txt`)     = meta1.format.println
      results(`meta_expression_zscore.txt`)  = meta2.format.println
      results(`data_expression_raw.txt`   )  = data1.format.println
      results(`data_expression_zscore.txt`)  = data2.format.println
    }
    
    
    // ===========================================================================
    results(`case_lists/cases_all.txt`) = caseListAll.format.println        
    results(`meta_study.txt`)           = study.format.println
    
    results(`meta_clinical_sample.txt`) = sampleMeta.format.println
    results(`data_clinical_sample.txt`) = sampleData.format.println

    cancerTypeOpt
      .map { cancerType =>
        results(`meta_cancer_type.txt`) = cancerType.formatMeta.println
        results(`data_cancer_type.txt`) = cancerType.formatData.println }
    
    
    // ---------------------------------------------------------------------------
    results.toMap
  }
  
}

// ===========================================================================
