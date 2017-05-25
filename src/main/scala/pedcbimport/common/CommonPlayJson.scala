package pedcbimport.common

import play.api.libs.json._
import pedcbimport.common.Wrapper._
import pedcbimport.cancertype.CancerType
import pedcbimport.study.Study

// ===========================================================================
object CommonPlayJson {
  
  implicit val JsonReadsAliquotId = new Reads[CancerTypeId] { def reads(jsValue: JsValue) = JsSuccess(CancerTypeId(jsValue.as[String])) }
  implicit val JsonReadsStudyId   = new Reads[StudyId     ] { def reads(jsValue: JsValue) = JsSuccess(StudyId     (jsValue.as[String])) }

  implicit val JsonWritesAliquotId = new Writes[CancerTypeId] { def writes(value: CancerTypeId) = JsString(value.format) }
  implicit val JsonWritesStudyId   = new Writes[StudyId     ] { def writes(value: StudyId     ) = JsString(value.format) }
  
  implicit val JsonReadsCancerType = Json.reads[CancerType]
  
  implicit val JsonReadsStudy   = Json.reads [Study]
  implicit val JsonWritesStudy  = Json.writes[Study]  
}

// ===========================================================================
