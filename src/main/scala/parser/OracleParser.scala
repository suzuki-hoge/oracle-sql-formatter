package parser

import scala.util.parsing.combinator.JavaTokenParsers

case class Keyword(value: String)

case class Col(value: String)

case class Cols(values: Col*)

sealed abstract class Value(val value: String)

case class StringValue(override val value: String) extends Value(value)

case class IntValue(override val value: String) extends Value(value)

case class Values(values: Value*)

case class Table(value: String)

trait OracleParser extends JavaTokenParsers {
  // functions
  def keyword(s: String) = s"(?i)$s".r ^^ (it => Keyword(it.toLowerCase))

  // commons
  private def oracleWord = "[a-zA-Z0-9_-]{1,30}".r

  def col = oracleWord ^^ Col

  def cols = repsep(col, ",") ^^ (it => Cols(it: _*))

  def value = stringValue | intValue // halfheartedly

  def stringValue = "'[a-zA-Z]+'".r ^^ (it => StringValue(it.replace("'", "")))

  def intValue = "[1-9][0-9]*".r ^^ IntValue

  def values = stringValues | intValues

  def stringValues = repsep(stringValue, ",") ^^ (it => Values(it: _*))

  def intValues = repsep(intValue, ",") ^^ (it => Values(it: _*))

  def table = oracleWord ^^ Table
}
