case class WhereResult(conditions: Seq[Condition])

trait Condition

case class ComparisonCondition(col: Col, operator: Operator, value: Value) extends Condition

case class InCondition(col: Col, not: Option[Keyword], values: Values) extends Condition

case class BetweenCondition(col: Col, not: Option[Keyword], value1: Value, value2: Value) extends Condition

case class IsCondition(col: Col, not: Option[Keyword]) extends Condition

case class PluralCondition(col: Col, operator: Operator, keyword: Keyword, values: Values) extends Condition

case class Operator(value: String)

trait WhereCondition extends OracleParser {
  def whereCondition = where ~> conditions ^^ (conditions => WhereResult(conditions))

  def where = keyword("WHERE")

  def condition = comparisonCondition | inCondition | betweenCondition | isCondition | pluralCondition

  def conditions = repsep(condition, and | or)

  def comparisonCondition = col ~ operator ~ value ^^ {
    case col ~ operator ~ value => ComparisonCondition(col, operator, value)
  }

  def inCondition = col ~ opt(not) ~ in ~ ("(" ~> values <~ ")") ^^ {
    case col ~ not ~ in ~ values => InCondition(col, not, values)
  }

  def betweenCondition = col ~ opt(not) ~ between ~ value ~ and ~ value ^^ {
    case col ~ not ~ b ~ v1 ~ a ~ v2 => BetweenCondition(col, not, v1, v2)
  }

  def isCondition = col ~ is ~ opt(not) <~ null_ ^^ {
    case col ~ i ~ not => IsCondition(col, not)
  }

  def pluralCondition = col ~ operator ~ (all | any | some) ~ ("(" ~> values <~ ")") ^^ {
    case col ~ operator ~ keyword ~ values => PluralCondition(col, operator, keyword, values)
  }


  def operator = ("=" | "<" | ">" | "<=" | ">=" | "!=" | "^=" | "<>") ^^ Operator

  def in = keyword("IN")

  def between = keyword("BETWEEN")

  def is = keyword("IS")

  def null_ = keyword("NULL")

  def all = keyword("ALL")

  def any = keyword("ANY")

  def some = keyword("SOME")

  def and = keyword("AND")

  def or = keyword("OR")

  def not = keyword("NOT")
}
