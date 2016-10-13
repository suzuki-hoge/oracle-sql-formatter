package formatter

import parser._

trait WhereFormatter extends OracleFormatter {
  def convert(result: WhereResult): String = {
    s"${Indent.current}WHERE\n${convert(result.conditions)}"
  }

  private def convert(conditions: Conditions): String = {
    (Seq(convert(conditions.head)) ++ conditions.tail.map(convert)).mkString(s"\n")
  }

  private def convert(condition: Condition): String = {
    Indent.++()

    val s = condition match {
      case c: ComparisonCondition => s"${Indent.current}${c.col.value} ${c.operator.value} ${convert(c.value)}"
      case c: InCondition => s"${Indent.current}${c.col.value} ${keyWithNot(c)} ${convert(c.values)}"
      case c: BetweenCondition => s"${Indent.current}${c.col.value} ${keyWithNot(c)} ${convert(c.value1)} AND ${convert(c.value2)}"
      case c: IsCondition => s"${Indent.current}${c.col.value} ${keyWithNot(c)} NULL"
      case c: PluralCondition => s"${Indent.current}${c.col.value} ${c.operator.value} ${convert(c.keyword)} ${convert(c.values)}"
    }

    Indent.--()

    s
  }

  private def convert_(condition: Condition): String = {
    condition match {
      case c: ComparisonCondition => s"${c.col.value} ${c.operator.value} ${convert(c.value)}"
      case c: InCondition => s"${c.col.value} ${keyWithNot(c)} ${convert(c.values)}"
      case c: BetweenCondition => s"${c.col.value} ${keyWithNot(c)} ${convert(c.value1)} AND ${convert(c.value2)}"
      case c: IsCondition => s"${c.col.value} ${keyWithNot(c)} NULL"
      case c: PluralCondition => s"${c.col.value} ${c.operator.value} ${convert(c.keyword)} ${convert(c.values)}"
    }
  }

  private def convert(tailCondition: (Keyword, Condition)): String = {
    Indent.++()
    val s = s"${Indent.current}${convert(tailCondition._1)} ${convert_(tailCondition._2)}"
    Indent.--()
    s
  }

  private def keyWithNot(condition: Condition): String = {
    def notOr(keyword: Option[Keyword]): String = {
      keyword match {
        case Some(x) => x.value.toUpperCase
        case None => ""
      }
    }

    def mkString(seq: Seq[String]): String = {
      seq.filterNot(it => it.isEmpty).mkString(" ")
    }

    condition match {
      case c: InCondition => mkString(Seq(notOr(c.not), "IN"))
      case c: BetweenCondition => mkString(Seq(notOr(c.not), "BETWEEN"))
      case c: IsCondition => mkString(Seq("IS", notOr(c.not)))
    }
  }
}
