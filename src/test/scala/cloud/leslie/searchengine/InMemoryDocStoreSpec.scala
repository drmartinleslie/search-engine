package cloud.leslie.searchengine

import org.scalatest.{FlatSpec, Matchers}

class InMemoryDocStoreSpec extends FlatSpec with Matchers {
  "InMemoryDocStore" should "take some inputs, return them and return docs for a keyword" in {
    val store = new InMemoryDocStore()
    SampleData.sampleDocs.foreach {
      case (id, keywords) => store.addDocument(id, keywords)
    }
    store.getDocFromId("1") shouldBe List("bread", "butter", "salt")
    store.getDocsForKeyword("salt") shouldBe Set("1", "3")
    store.getDocsForKeyword("cream") shouldBe Set("2")
  }
}
