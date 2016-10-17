import formatter.{Indent, SelectFormatter}
import parser.SelectParser

object SimpleFormatter {
  def select(origin: String): Either[String, String] = {
    SelectParser(origin).right.map(SelectFormatter.convert(_)(new Indent))
  }
}
