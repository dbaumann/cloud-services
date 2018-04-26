package com.summitcove.theproxy

import java.io.{InputStream, OutputStream}

import scala.io.Source
import scala.util.Try

private[theproxy] object Encoding {
  import io.circe._
  import io.circe.parser._
  import io.circe.syntax._

  // decode
  def in[T](is: InputStream)(implicit decoder: Decoder[T]): Try[T] = {
    val t = Try(Source.fromInputStream(is).mkString).flatMap(decode[T](_).toTry)
    is.close()
    t
  }

  // encode
  def out[T](value: T, os: OutputStream)(implicit encoder: Encoder[T]): Try[Unit] = {
    val t = Try(os.write(value.asJson.noSpaces.getBytes("UTF-8")))
    os.close()
    t
  }

  def deserialize(is: InputStream): Try[Json] = {
    val t = Try(Source.fromInputStream(is).mkString).flatMap(parse(_).toTry)
    is.close()
    t
  }

  def serialize(value: Json, os: OutputStream): Try[Unit] = {
    val t = Try(os.write(value.asJson.noSpaces.getBytes("UTF-8")))
    os.close()
    t
  }
}