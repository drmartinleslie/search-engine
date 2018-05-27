package cloud.leslie.searchengine

import cloud.leslie.searchengine.Requests.{And, IndexRequest, Or, Term}
import fastparse.core.Parsed
import org.scalatest.{FlatSpec, Matchers}

class RequestParserSpec extends FlatSpec with Matchers {
  "query" should "parse a literal" in {
    RequestParser.query.parse("query hello1").get.value shouldBe Term("hello1")
  }

  it should "parse a conjunction of literals" in {
    RequestParser.query.parse("query hello & goodbye").get.value shouldBe And(Term("hello"), Term("goodbye"))
  }

  it should "parse a conjunction of a disjunction and a literal" in {
    RequestParser.query.parse("query (butter | potato) & salt").get.value shouldBe
      And(Or(Term("butter"), Term("potato")), Term("salt"))
  }

  it should "fail on malformed string" in {
    val failure = RequestParser.query.parse("query (butter |")
    failure shouldBe a[Parsed.Failure[_, _]]
  }

  "index" should "parse an index request" in {
    RequestParser.index.parse("index 1 butter potato").get.value shouldBe
      IndexRequest("1", List("butter", "potato"))
  }

  it should "fail on malformed string" in {
    val failure = RequestParser.index.parse("index butter")
    failure shouldBe a[Parsed.Failure[_, _]]
  }
}
