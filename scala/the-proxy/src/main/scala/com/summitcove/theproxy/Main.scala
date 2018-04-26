package com.summitcove.theproxy

import com.amazonaws.services.lambda.runtime.Context

import scala.concurrent.Future
import io.circe.generic.auto._
import io.github.yeghishe.lambda._
import com.softwaremill.sttp._
import org.apache.log4j.Logger

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._, io.circe.optics.JsonPath._

class ProxyHandler extends JsonHandler {
  def handler(json: Json, context: Context): Json = {
    val target = sys.env("TARGET_URI")

    implicit val backend = HttpURLConnectionBackend()

    logger.info(s"sending event to $target")

    sttp.post(uri"$target").body(expandDotted(json)).send()

    Json.Null
  }

  // https://stackoverflow.com/a/44059753
  val Dotted = "([^\\.]*)\\.(.*)".r
  protected[theproxy] def expandDotted(j: Json): Json = j.arrayOrObject(
    j,
    js => Json.fromValues(js.map(expandDotted)),
    _.toList.map {
      case (Dotted(k, rest), v) => Json.obj(k -> expandDotted(Json.obj(rest -> v)))
      case (k, v) => Json.obj(k -> expandDotted(v))
    }.reduceOption(_.deepMerge(_)).getOrElse(Json.obj())
  )

  implicit val jsonSerializer: BodySerializer[Json] = { json: Json =>
    StringBody(json.toString, "UTF-8", Some("application/json"))
  }
}
