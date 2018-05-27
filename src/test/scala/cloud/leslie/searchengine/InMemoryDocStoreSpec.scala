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

  // TODO: Delete or ignore or mark as performance test?
  "InMemoryDocStore and CoordinatingFunctions.query" should "have fast querying for 10000s of docs" in {
    val store = new InMemoryDocStore()
    val indexStart = System.currentTimeMillis()
    (0 to 49999).foreach { i =>
      val keywords = List("a" + i, "b" + i%10, "c" + i%100, "d" + i%1000, "e" + i%10000)
      store.addDocument(i.toString, keywords)
    }
    val indexEnd = System.currentTimeMillis()
    println(s"Indexing 50000 items took ${(indexEnd - indexStart) / 1000.0 } seconds")

    val requestString = "query a1 | (b2 & c52)"
    val queryStart = System.currentTimeMillis()
    val response = CoordinatingFunctions.query(store, requestString)
    val queryEnd = System.currentTimeMillis()
    println(s"Querying took ${(queryEnd - queryStart) / 1000.0 } seconds")
  }
}
