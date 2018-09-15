package org.dbaumann.theproxy

import org.scalatest._
import io.circe._
import io.circe.parser._


class MainSpec extends FlatSpec {
  def environmentAsMap: java.util.Map[String, String] = {
    val field = System.getenv().getClass.getDeclaredField("m")
    field.setAccessible(true)
    field.get(System.getenv()).asInstanceOf[java.util.Map[String, String]]
  }

  "expandDotted" should "produce a tree from a map" in {
    val handler = new ProxyHandler()

    var input = parse("""
    {
      "methodName": "CreateWorkOrder",
      "params.company_code": "6f902e1f18fe45c2a8c5b831e81121c",
      "params.processor_id": "105961",
      "params.unit_id": 41369,
      "params.title": "test",
      "params.description": "test"
    }
    """).getOrElse(Json.Null)

    var result = handler.expandDotted(input)

    var expected = parse("""
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

    // should also work for every object in a json array
    input = parse("""
    [
      {
        "methodName": "CreateWorkOrder",
        "params.company_code": "6f902e1f18fe45c2a8c5b831e81121c",
        "params.processor_id": "105961",
        "params.unit_id": 41369,
        "params.title": "test",
        "params.description": "test"
      }
    ]
    """).getOrElse(Json.Null)

    result = handler.expandDotted(input)

    expected = parse("""
    [
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
    ]
    """).getOrElse(Json.Null)

    assert(result === expected)

  }

  "mergeEnvironment" should "merge underscore-prefixed environment variables into the request body" in {

    environmentAsMap.clear()
    environmentAsMap.put("_foo_bar", "baz")

    val handler = new ProxyHandler()

    var input = parse("""
    {
      "methodName": "CreateWorkOrder",
      "params.company_code": "6f902e1f18fe45c2a8c5b831e81121c"
    }
    """).getOrElse(Json.Null)

    var result = handler.mergeEnvironment(input)

    var expected = parse("""
    {
      "methodName": "CreateWorkOrder",
      "params.company_code": "6f902e1f18fe45c2a8c5b831e81121c",
      "foo.bar": "baz"
    }
    """).getOrElse(Json.Null)

    assert(result === expected)

    // should also work for every object in a json array
    input = parse("""
    [
      {
        "methodName": "CreateWorkOrder",
        "params.company_code": "6f902e1f18fe45c2a8c5b831e81121c"
      }
    ]
    """).getOrElse(Json.Null)

    result = handler.mergeEnvironment(input)

    expected = parse("""
    [
      {
        "methodName": "CreateWorkOrder",
        "params.company_code": "6f902e1f18fe45c2a8c5b831e81121c",
        "foo.bar": "baz"
      }
    ]
    """).getOrElse(Json.Null)

    assert(result === expected)
  }
}
