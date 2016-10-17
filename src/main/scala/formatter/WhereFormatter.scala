package formatter

import parser._

object ComparisonConditionOf extends OracleFormatter {
  def unapply(c: ComparisonCondition): Option[(String, String, String)] = c match {
    case ComparisonCondition(Col(col), Operator(ope), v) => Some(col, ope, convert(v))
    case _ => None
  }
}

object InConditionOf extends OracleFormatter {
  def unapply(c: InCondition): Option[(String, String, String)] = c match {
    case InCondition(Col(col), not, vs) => Some(col, not.fold("IN")(_ => "NOT IN"), convert(vs))
    case _ => None
  }
}

object BetweenConditionOf extends OracleFormatter {
  def unapply(c: BetweenCondition): Option[(String, String, String, String)] = c match {
    case BetweenCondition(Col(col), not, v1, v2) => Some(col, not.fold("BETWEEN")(_ => "NOT BETWEEN"), convert(v1), convert(v2))
    case _ => None
  }
}

object IsConditionOf extends OracleFormatter {
  def unapply(c: IsCondition): Option[(String, String)] = c match {
    case IsCondition(Col(col), not) => Some(col, not.fold("IS")(_ => "IS NOT"))
    case _ => None
  }
}

object PluralConditionOf extends OracleFormatter {
  def unapply(c: PluralCondition): Option[(String, String, String, String)] = c match {
    case PluralCondition(Col(col), Operator(ope), key, vs) => Some(col, ope, convert(key), convert(vs))
    case _ => None
  }
}

trait WhereFormatter extends OracleFormatter {
  def convert(result: WhereResult): String = {
    s"${indent}WHERE\n${convert(result.conditions)}"
  }

  private def convert(conditions: Conditions): String = {
    (Seq(head(conditions.head)) ++ conditions.tail.map(tail)).mkString(s"\n")
  }

  private def head(condition: Condition): String = {
    >>((condition: Condition) => convertWith(__, condition), condition)
  }

  private def tail(tailCondition: (Keyword, Condition)): String = {
    >>((tailCondition: (Keyword, Condition)) => s"${indent}${convert(tailCondition._1)} ${convertWith("", tailCondition._2)}", tailCondition)
  }

  private def convertWith(indent: String, condition: Condition): String = condition match {
    case ComparisonConditionOf(col, ope, v) => s"$indent$col $ope $v"
    case InConditionOf(col, key, vs) => s"$indent$col $key $vs"
    case BetweenConditionOf(col, key, v1, v2) => s"$indent$col $key $v1 AND $v2"
    case IsConditionOf(col, key) => s"$indent$col $key NULL"
    case PluralConditionOf(col, ope, key, vs) => s"$indent$col $ope $key $vs"
  }
}
