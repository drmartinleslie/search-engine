package cloud.leslie.searchengine

import cloud.leslie.searchengine.Requests.{And, Or, Term}
import org.scalatest.{FlatSpec, Matchers}

class RequestsSpec extends FlatSpec with Matchers {

  val store = new InMemoryDocStore()
  SampleData.sampleDocs.foreach {
    case (id, keywords) => store.addDocument(id, keywords)
  }

  "doQuery" should "succeed with basic query" in {
    val query = Term("butter")
    val result = Requests.doQuery(query, store)
    result shouldBe Set("1", "2")
  }

  it should "succeed with more complicated query" in {
    val query = And(Or(Term("butter"), Term("potato")), Term("salt"))
    val result = Requests.doQuery(query, store)
    result shouldBe Set("1", "3")
  }

}
