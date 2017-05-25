package pedcbimport.study

import play.api.libs.json.JsArray
import play.api.libs.json.Json
import utils.Implicits.StringImplicits

// ===========================================================================
object Study2 {
  
  def main(args: Array[String]): Unit = {
    val studies = apply("/tmp/studies.tsv")

    "/tmp/studies.json"
      .path
      .writeFile(
        Json.prettyPrint(studies))
    
  }
  
  // ===========================================================================
  def apply(studyTsvFilePath: String): JsArray = {   
    val studies: Seq[Study] = 
      studyTsvFilePath
        .path
        .readTsvFile()
        .drop(1) // TODO: check header though
        .filterNot(_.isEmpty)
        .map(_.iterator)
        .map(RawStudy.apply)
        .map(Study.fromRawStudy)
    
    import pedcbimport.pipelines.common.CommonPlayJson._        
    Json.toJson(studies).as[JsArray]
  }
  
}

// ===========================================================================