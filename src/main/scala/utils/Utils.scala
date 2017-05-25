package utils

import play.api.libs.json.JsValue
import play.api.libs.json.JsObject
import utils.Implicits.StringImplicits

// ===========================================================================
object Utils {
  
  def formatJsValue(jsValue: JsValue): String =
    jsValue
      .as[JsObject]
      .value
      .mapValues(_.toString)
      .mapValues(v => v.unquote.getOrElse(v))
      .map { case (key, value) =>
        s"${key}: ${value.replace("\\t", "\t")}" }
      .mkString("\n")
      .suffixWithNewLine

}

// ===========================================================================
