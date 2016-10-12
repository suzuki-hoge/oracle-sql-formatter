package parser

import scala.util.parsing.combinator.JavaTokenParsers

case class Keyword(value: String)

case class Bracketed(value: Any)

case class Col(value: String)

case class Cols(values: Seq[String])

case class Value(value: String)

case class Values(values: Seq[String])

case class Table(name: String)

trait OracleParser extends JavaTokenParsers {
  // functions
  def keyword(s: String) = "(?i)%s".format(s).r ^^ (it => Keyword(it.toLowerCase))

  def rec(e: Parser[Any], sep: Parser[Any] = ",") = e ~ rep(sep ~ e)

  // commons
  private def oracleWord = "[a-zA-Z0-9_-]{1,30}".r

  def col = oracleWord ^^ Col

  def cols = repsep(col, ",") ^^ (they => Cols(they.map(_.value)))

  def value = ("'[a-zA-Z]+'".r | "[1-9][0-9]*".r) ^^ (it => Value(it.replace("'", ""))) // halfheartedly

  def values = repsep(value, ",") ^^ (they => Values(they.map(_.value)))

  def table = oracleWord ^^ Table
}
