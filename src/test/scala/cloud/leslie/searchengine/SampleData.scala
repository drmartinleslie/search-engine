package cloud.leslie.searchengine

object SampleData {
  val sampleIndexStrings = IndexedSeq(
    "index 1 soup tomato cream salt",
    "index 2 cake sugar eggs flour sugar cocoa cream butter",
    "index 1 bread butter salt",
    "index 3 soup fish potato salt pepper"
  )

  val sampleQueryStringsAndResults = IndexedSeq(
    ("query butter", Set("query results 2 1", "query results 1 2")),
    ("query sugar", Set("query results 2")),
    ("query soup", Set("query results 3")),
    ("query (butter | potato) & salt", Set("query results 1 3", "query results 3 1"))
  )

  val sampleDocs = IndexedSeq(
    ("1", List("soup", "tomato", "cream", "salt")),
    ("2", List("cake", "sugar", "eggs", "flour", "sugar", "cocoa", "cream", "butter")),
    ("1", List("bread", "butter", "salt")),
    ("3", List("soup", "fish", "potato", "salt", "pepper"))
  )
}
