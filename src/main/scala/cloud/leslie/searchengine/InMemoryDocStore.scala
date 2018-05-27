package cloud.leslie.searchengine
import scala.collection.mutable

class InMemoryDocStore {

  private val docs = mutable.Map.empty[String, List[String]]
  private val keywords = mutable.Map.empty[String, Set[String]]

  def addDocument(id: String, keywordsToAdd: List[String]): Unit = {

    // remove any existing keywords from keywords map
    docs.get(id).foreach { existingKeywords =>
      existingKeywords.toSet.foreach { kw: String =>
        val existingDocIds = keywords(kw)
        if (existingDocIds.size == 1) {
          keywords -= kw
        } else {
          keywords(kw) = existingDocIds - id
        }
      }
    }

    // store new doc
    docs(id) = keywordsToAdd

    // add keywords to keywords map
    keywordsToAdd.toSet.foreach { keyword: String =>
      keywords.get(keyword) match {
        case Some(existingDocIds) =>
          keywords(keyword) = existingDocIds + id
        case None =>
          keywords(keyword) = Set(id)
      }
    }
  }

  def getDocsForKeyword(keyword: String): Set[String] = {
    keywords.getOrElse(keyword, Set())
  }

  def getDocFromId(id: String): List[String] = {
    docs.getOrElse(id, Nil)
  }

}
