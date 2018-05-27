package cloud.leslie.searchengine
import CommandLineInterface._
import Commands._
import cloud.leslie.searchengine.Requests.RequestResponse

object Main extends App {
  import CoordinatingFunctions._
  val store = new InMemoryDocStore()
  var done = false
  displayWelcome()
  while (!done) {
    receiveCommand() match {
      case Help =>
        displayHelp()
      case Exit =>
        done = true
      case Index(text) =>
        val response = index(store, text)
        displayText(response.message)
      case Query(text) =>
        val response = query(store, text)
        displayText(response.message)
      case Unknown =>
        displayInvalidCommand()
    }
  }
}

object CoordinatingFunctions {
  def index(store: InMemoryDocStore, indexRequest: String): RequestResponse =
    RequestParser.parseIndexRequestAndHandle(Requests.indexError, Requests.indexAndRespond(store))(indexRequest)

  def query(store: InMemoryDocStore, queryRequest: String): RequestResponse =
    RequestParser.parseQueryRequestAndHandle(Requests.queryError, Requests.queryAndRespond(store))(queryRequest)
}