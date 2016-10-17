package formatter

import parser._

object ComparisonConditionOf extends OracleFormatter {
  def unapply(c: ComparisonCondition): Option[(String, String, String)] = c match {
    case ComparisonCondition(Col(col), Operator(ope), v) => Some(col, ope, convert(v))
    case _ => None
  }
}

object InConditionOf extends OracleFormatter {
  def unapply(c: InCondition)(implicit indent:Indent): Option[(String, String, String)] = c match {
    case InCondition(Col(col), not, vs) => Some(col, not.fold("IN")(_ => "NOT IN"), convert(vs)(indent))
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
  def unapply(c: PluralCondition)(implicit indent:Indent): Option[(String, String, String, String)] = c match {
    case PluralCondition(Col(col), Operator(ope), key, vs) => Some(col, ope, convert(key), convert(vs)(indent))
    case _ => None
  }
}

trait WhereFormatter extends OracleFormatter {
  def convert(result: WhereResult)(indent:Indent): String = {
    s"${indent}WHERE\n${convert(result.conditions)(indent)}"
  }

  private def convert(conditions: Conditions)(indent:Indent): String = {
    (Seq(head(conditions.head)(indent)) ++ conditions.tail.map(tail(_)(indent))).mkString(s"\n")
  }

  private def head(condition: Condition)(indent:Indent): String = {
    >>(convertWith(indent.toString, condition)(indent.inc))(indent)
//    >>((condition: Condition) => convertWith(indent.toString, condition), condition)
  }

  private def tail(tailCondition: (Keyword, Condition))(indent:Indent): String = {
    >>(s"${indent}${convert(tailCondition._1)} ${convertWith("", tailCondition._2)(indent)}")(indent)
//    >>((tailCondition: (Keyword, Condition)) => s"${indent}${convert(tailCondition._1)} ${convertWith("", tailCondition._2)}", tailCondition)
  }

  private def convertWith(preindent: String, condition: Condition)(implicit indent:Indent): String = condition match {
    case ComparisonConditionOf(col, ope, v) => s"$preindent$col $ope $v"
    case InConditionOf(col, key, vs) => s"$preindent$col $key $vs"
    case BetweenConditionOf(col, key, v1, v2) => s"$preindent$col $key $v1 AND $v2"
    case IsConditionOf(col, key) => s"$preindent$col $key NULL"
    case PluralConditionOf(col, ope, key, vs) => s"$preindent$col $ope $key $vs"
  }
}
