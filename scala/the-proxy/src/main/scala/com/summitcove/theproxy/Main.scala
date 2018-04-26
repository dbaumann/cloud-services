package com.summitcove.theproxy

import com.amazonaws.services.lambda.runtime.Context

import scala.concurrent.Future
import io.circe.generic.auto._
import io.github.yeghishe.lambda._
import com.softwaremill.sttp._
import org.apache.log4j.Logger
import io.circe._, io.circe.parser._

// handler io.github.yeghishe.MySimpleHander::handler
// input "foo"
object MySimpleHander extends App {
  def handler(rawJson: String, context: Context): Unit = {
    val logger = Logger.getLogger(MySimpleHander.getClass)

    parse(rawJson) match {
      case Left(failure) =>
        logger.info("Invalid JSON :(")
      case Right(json) =>
        logger.info(json.as[Map[String, String]])
    }
  }
}

case class Name(name: String)
case class Greeting(message: String)

// handler io.github.yeghishe.MyHandler
// input {"name": "Yeghishe"}
class MyHandler extends Handler[Name, Greeting] {
  def handler(name: Name, context: Context): Greeting = {
    implicit val backend = HttpURLConnectionBackend()
    sttp.get(uri"https://webhook.site/34cc6d7f-5212-4d9f-92a0-5f6365322071").send()

    logger.info(s"Name is $name")
    Greeting(s"Hello ${name.name}")
  }
}

class ProxyHandler extends JsonHandler {
  def handler(json: Json, context: Context): Json = {
    implicit val backend = HttpURLConnectionBackend()
    
    logger.info(json)
    sttp.get(uri"https://webhook.site/34cc6d7f-5212-4d9f-92a0-5f6365322071").send()
    json
  }
}