package org.dbaumann.theproxy


import java.io.{InputStream, OutputStream}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import io.circe.{Decoder, Encoder, Json}
import org.apache.log4j.Logger

import scala.io.Source
import scala.util.Try

trait JsonHandler extends RequestStreamHandler {
  import JsonEncoding._

  protected implicit val logger = Logger.getLogger(this.getClass)

  protected def handler(input: Json, context: Context): Json
  def handleRequest(is: InputStream, os: OutputStream, context: Context): Unit =
    deserialize(is).flatMap(i => serialize(handler(i, context), os)).get
}
