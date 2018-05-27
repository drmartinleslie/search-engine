package cloud.leslie.searchengine

object Requests {
  sealed trait QueryRequest
  case class Term(s: String) extends QueryRequest
  case class And(l: QueryRequest, r: QueryRequest) extends QueryRequest
  case class Or(l: QueryRequest, r: QueryRequest) extends QueryRequest

  case class IndexRequest(id: String, keywords: List[String])

  case class RequestResponse(isSuccess: Boolean, message: String)

  def queryError(error: String): RequestResponse =
    RequestResponse(isSuccess = false, "query error: " + error)

  def queryAndRespond(store: InMemoryDocStore)(queryRequest: QueryRequest): RequestResponse = {
    val results = Requests.doQuery(queryRequest, store)
    RequestResponse(isSuccess = true, s"query results ${results.mkString(" ")}")
  }

  def doQuery(q: QueryRequest, store: InMemoryDocStore): Set[String] = {
    q match {
      case Term(s) => store.getDocsForKeyword(s)
      case And(l, r) => doQuery(l, store).intersect(doQuery(r, store))
      case Or(l, r) => doQuery(l, store).union(doQuery(r, store))
    }
  }

  def indexError(error: String): RequestResponse =
    RequestResponse(isSuccess = false, "index error: " + error)

  def indexAndRespond(store: InMemoryDocStore)(indexRequest: IndexRequest): RequestResponse = {
    Requests.doIndex(indexRequest, store)
    RequestResponse(isSuccess = true, s"index ok ${indexRequest.id}")
  }

  def doIndex(indexRequest: IndexRequest, store: InMemoryDocStore): Unit = {
    store.addDocument(indexRequest.id, indexRequest.keywords)
  }

}
