package com.summitcove.theproxy

import org.scalatest._
import io.circe._
import io.circe.parser._

class MainSpec extends FlatSpec {
  "expandDotted" should "produce a tree from a map" in {
    val handler = new ProxyHandler()
    val input = parse("""
    {
      "methodName": "CreateWorkOrder",
      "params.company_code": "6f902e1f18fe45c2a8c5b831e81121c",
      "params.processor_id": "105961",
      "params.unit_id": 41369,
      "params.title": "test",
      "params.description": "test"
    }
    """).getOrElse(Json.Null)

    val result = handler.expandDotted(input)
    
    val expected = parse("""
    {
      "methodName": "CreateWorkOrder",
      "params": {
        "company_code": "6f902e1f18fe45c2a8c5b831e81121c",
        "processor_id": "105961",
        "unit_id": 41369,
        "title": "test",
        "description": "test"
      }
    }
    """).getOrElse(Json.Null)

    assert(result === expected)
  }
}