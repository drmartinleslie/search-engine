package cloud.leslie.searchengine

import org.scalatest.{FlatSpec, Matchers}

class CoordinatingFunctionsSpec extends FlatSpec with Matchers {
  "index and query" should "work for sample data" in {
    val store = new InMemoryDocStore()
    SampleData.sampleIndexStrings.foreach {
      text =>
        val docId = text(6) // e.g. "index 5 rice" so the docId is in posn 6
        val result = CoordinatingFunctions.index(store, text)
        result.message shouldBe s"index ok $docId"
    }
    SampleData.sampleQueryStringsAndResults.foreach {
      case (queryText, allowableResults) =>
        val result = CoordinatingFunctions.query(store, queryText)
        allowableResults.contains(result.message) shouldBe true
    }
  }
}
