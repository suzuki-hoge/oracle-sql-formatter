

trait WhereCondition extends OracleParser {
  def whereCondition = where ~ conditions

  def where = keyword("WHERE")

  def condition = comparisonCondition | inCondition | betweenCondition | isCondition | pluralCondition

  def conditions = rec(condition, and | or)

  def comparisonCondition = validName ~ operator ~ value

  def inCondition = validName ~ opt(not) ~ in ~ brc(values)

  def betweenCondition = validName ~ opt(not) ~ between ~ value ~ and ~ value

  def isCondition = validName ~ is ~ opt(not) ~ null_

  def pluralCondition = validName ~ operator ~ (all | any | some) ~ brc(values)


  def operator = "=" | "<" | ">" | "<=" | ">=" | "!=" | "^=" | "<>"

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
