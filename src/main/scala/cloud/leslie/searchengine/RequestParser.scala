package cloud.leslie.searchengine
import cloud.leslie.searchengine.Requests.{And, Or, QueryRequest, Term, IndexRequest}
import fastparse.all._

object RequestParser {
  private val space = P(" ").rep(1)
  private val optionalSpace = P(" ").rep()
  private val alphanumeric: P[String] = P(CharIn(('0' to '9') ++ ('a' to 'z') ++ ('A' to 'Z')).rep(1).!)
  private val literalTerm: P[Term] = alphanumeric.map(s => Term(s))
  private val parens: P[QueryRequest] = P("(" ~ optionalSpace ~ (conjunction | disjunction) ~ optionalSpace ~ ")")
  private val term: P[QueryRequest] = P(literalTerm | parens)
  private val conjunction: P[And] = P(term ~ optionalSpace ~ "&" ~ optionalSpace ~ term).map { case (l, r) => And(l, r) }
  private val disjunction: P[Or] = P(term ~ optionalSpace ~ "|" ~ optionalSpace ~ term).map { case (l, r) => Or(l, r)}
  val query: P[QueryRequest] = P("query" ~ space ~ (conjunction | disjunction | literalTerm) ~ End)

  private val integerId: P[String] = P((CharIn('1' to '9') ~ CharIn('0' to '9').rep()).!) // String, so very large ids are possible
  val index: P[IndexRequest] = P("index" ~ space ~ integerId ~ (space ~ alphanumeric).rep() ~ End).map {
    case (id, keywords) => IndexRequest(id, keywords.toList)
  }

  // a more convenient function for our purposes than the built-in fold method
  def handle[T, X](onFailure: String => X, onSuccess: T => X)(parsed: Parsed[T]): X = {
    parsed.fold[X](
      { (_, posn, extra) =>
          val error = extra.input.repr.errorMessage(extra.input, extra.traced.expected, posn)
          onFailure(error)
      },
      (result, _) =>
        onSuccess(result)
    )
  }

  def parseQueryRequestAndHandle[X](onFailure: String => X, onSuccess: QueryRequest => X): String => X =
    (request: String) => handle(onFailure, onSuccess)(query.parse(request))

  def parseIndexRequestAndHandle[X](onFailure: String => X, onSuccess: IndexRequest => X): String => X =
    (request: String) => handle(onFailure, onSuccess)(index.parse(request))

}
