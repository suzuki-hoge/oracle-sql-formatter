import formatter.SelectFormatter
import parser.SelectParser

object SimpleFormatter {
  def select(origin: String): Either[String, String] = {
    SelectParser(origin).right.map(SelectFormatter.convert)
  }
}
