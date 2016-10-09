import scala.util.parsing.combinator.JavaTokenParsers

trait OracleParser extends JavaTokenParsers {
  // functions
  def keyword(s: String) = "(?i)%s".format(s).r

  def rec(e: Parser[Any], sep: Parser[Any] = ",") = e ~ rep(sep ~ e)

  def brc(e: Parser[Any]) = "(" ~ e ~ ")"

  // commons
  def validName = "[a-zA-Z0-9_-]{1,30}".r

  def validNames = rec(validName)

  def tableName = validName

  def value = "'[a-zA-Z]+'".r | "[1-9][0-9]*".r // halfheartedly

  def values = rec(value)
}
