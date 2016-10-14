package formatter

import parser._

trait WhereFormatter extends OracleFormatter {
  def convert(result: WhereResult): String = {
    s"${__}WHERE\n${convert(result.conditions)}"
  }

  private def convert(conditions: Conditions): String = {
    (Seq(head(conditions.head)) ++ conditions.tail.map(tail)).mkString(s"\n")
  }

  private def head(condition: Condition): String = {
    >>((condition: Condition) => convertWith(__, condition), condition)
  }

  private def tail(tailCondition: (Keyword, Condition)): String = {
    >>((tailCondition: (Keyword, Condition)) => s"${__}${convert(tailCondition._1)} ${convertWith("", tailCondition._2)}", tailCondition)
  }

  private def convertWith(indent: String, condition: Condition): String = {
    condition match {
      case ComparisonCondition(col, ope, value) => s"$indent${col.value} ${ope.value} ${convert(value)}"
      case InCondition(col, not, values) => s"$indent${col.value} ${keyWithNot("in", not)} ${convert(values)}"
      case BetweenCondition(col, not, v1, v2) => s"$indent${col.value} ${keyWithNot("between", not)} ${convert(v1)} AND ${convert(v2)}"
      case IsCondition(col, not) => s"$indent${col.value} ${keyWithNot("is", not)} NULL"
      case PluralCondition(col, ope, key, values) => s"$indent${col.value} ${ope.value} ${convert(key)} ${convert(values)}"
    }
  }

  private def keyWithNot(key: String, not: Option[Keyword]): String = {
    not match {
      case Some(_) => key match {
        case "in" => "NOT IN"
        case "between" => "NOT BETWEEN"
        case "is" => "IS NOT"
      }
      case None => key match {
        case "in" => "IN"
        case "between" => "BETWEEN"
        case "is" => "IS"
      }
    }
  }
}
