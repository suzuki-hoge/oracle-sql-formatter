package parser

import scala.util.parsing.combinator.JavaTokenParsers

case class Keyword(value: String)

case class Col(value: String)

case class Cols(values: Seq[Col])

trait Value

case class StringValue(value: String) extends Value

case class IntValue(value: String) extends Value

trait Values

case class StringValues(values: Seq[StringValue]) extends Values

case class IntValues(values: Seq[IntValue]) extends Values

case class Table(value: String)

trait OracleParser extends JavaTokenParsers {
  // functions
  def keyword(s: String) = "(?i)%s".format(s).r ^^ (it => Keyword(it.toLowerCase))

  // commons
  private def oracleWord = "[a-zA-Z0-9_-]{1,30}".r

  def col = oracleWord ^^ Col

  def cols = repsep(col, ",") ^^ Cols

  def value = stringValue | intValue // halfheartedly

  def stringValue = "'[a-zA-Z]+'".r ^^ (it => StringValue(it.replace("'", "")))

  def intValue = "[1-9][0-9]*".r ^^ IntValue

  def values = stringValues | intValues

  def stringValues = repsep(stringValue, ",") ^^ StringValues

  def intValues = repsep(intValue, ",") ^^ IntValues

  def table = oracleWord ^^ Table
}
