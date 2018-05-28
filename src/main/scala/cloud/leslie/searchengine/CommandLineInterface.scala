package cloud.leslie.searchengine

object CommandLineInterface {
  import Commands._

  def displayWelcome() =
    println(InterfaceStrings.welcomeString)

  def receiveCommand(): Command = {
    print("> ")
    val input = scala.io.StdIn.readLine()
    if (input == "help")
      Help
    else if (input == "exit")
      Exit
    else if (input.startsWith("index"))
      Index(input)
    else if (input.startsWith("query"))
      Query(input)
    else
      Unknown
  }

  def displayHelp() =
    println(InterfaceStrings.helpString)

  def displayInvalidCommand() =
    println("Invalid command")

  def displayText(text: String) =
    println(text)

}

object Commands {
  sealed trait Command
  case object Help extends Command
  case object Exit extends Command
  case class Index(text: String) extends Command
  case class Query(text: String) extends Command
  case object Unknown extends Command
}

object InterfaceStrings {

  val welcomeString = "Welcome to Search Engine. Enter 'help' for help. Enter 'exit' to exit."

  val helpString =
    """
      |Search Engine is an in-memory store of ids and keywords that lets you efficiently search for ids by keywords. There is no persistence of data between sessions and the store is empty upon startup.
      |
      |Legal commands:
      |
      |help
      |     Show this help text.
      |
      |exit
      |     Exit Search Engine.
      |
      |index <id> <keyword1> <keyword2> ...
      |     Store a document id (positive integer) and a list of keywords (case sensitive alphanumeric). For example 'index 1 soup tomato cream salt'. This will replace any previous document with the same id.
      |
      |query <keyword>
      |     Query for ids of all documents with a given keyword. For example 'query tomato'.
      |
      |query <keyword1> & <keyword2>
      |     Query for ids of all documents with both keywords. For example 'query tomato & soup'.
      |
      |query <keyword1> | <keyword2>
      |     Query for ids of all documents with either keyword. For example 'query tomato | soup'.
      |
      |query <expression1> <operator> <expression2>
      |     Queries with & and | can include nested & or | expressions. Nested queries must have parentheses but the overall query and literal keywords must not. For example 'query (butter | potato) & salt' or 'query (butter & potato) | (chicken & salt)'.
    """.stripMargin

}