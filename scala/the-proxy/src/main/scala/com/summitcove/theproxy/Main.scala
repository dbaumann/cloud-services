package com.summitcove.theproxy

import com.amazonaws.services.lambda.runtime.Context

import scala.concurrent.Future
import io.circe.generic.auto._
import io.github.yeghishe.lambda._
import com.softwaremill.sttp._

// handler io.github.yeghishe.MySimpleHander::handler
// input "foo"
object MySimpleHander extends App {
  def handler(name: String, context: Context): String = s"Hello $name"
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
