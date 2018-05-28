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

  it should "parse a conjunction of disjunctions with some strange spaces and alphanumeric lower/upper-case keywords" in {
    RequestParser.query.parse("query (Butt23er | poTato) &(salt| pepper )").get.value shouldBe
      And(Or(Term("Butt23er"), Term("poTato")), Or(Term("salt"),  Term("pepper")))
  }

  it should "parse a more complicated nested query" in {
    RequestParser.query.parse("query (hello & goodbye) | (23 & (78 | world))").get.value shouldBe
      Or(And(Term("hello"), Term("goodbye")), And(Term("23"), Or(Term("78"), Term("world"))))
  }

  it should "fail if we leave out a keyword" in {
    RequestParser.query.parse("query butter |") shouldBe a[Parsed.Failure[_, _]]
  }

  it should "fail if we have mismatched parentheses" in {
    RequestParser.query.parse("query butter | potato & salt)") shouldBe a[Parsed.Failure[_, _]]
  }

  it should "fail if we do not have required parentheses" in {
    RequestParser.query.parse("query butter & potato & salt") shouldBe a[Parsed.Failure[_, _]]
  }

  it should "fail if we have non-alphanumeric keyword" in {
    RequestParser.query.parse("query not-butter") shouldBe a[Parsed.Failure[_, _]]
  }

  "index" should "parse an index request with multidigit id and alphanumeric keywords" in {
    RequestParser.index.parse("index 1234 butter23 potato").get.value shouldBe
      IndexRequest("1234", List("butter23", "potato"))
  }

  it should "parse an index request with single digit id and zero keywords" in {
    RequestParser.index.parse("index 1").get.value shouldBe
      IndexRequest("1", Nil)
  }

  it should "fail on string without integer" in {
    RequestParser.index.parse("index butter") shouldBe a[Parsed.Failure[_, _]]
  }

  it should "fail on string with malformed integer" in {
    RequestParser.index.parse("index 0123 butter") shouldBe a[Parsed.Failure[_, _]]
  }

  it should "fail on string with non-alphanumeric keyword" in {
    RequestParser.index.parse("index 123 not_butter") shouldBe a[Parsed.Failure[_, _]]
  }

  it should "fail on string with missing space" in {
    RequestParser.index.parse("index123 butter") shouldBe a[Parsed.Failure[_, _]]
  }

}
