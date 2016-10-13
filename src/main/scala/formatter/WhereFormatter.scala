package formatter

import parser._

trait WhereFormatter extends OracleFormatter {
  def convert(result: WhereResult): String = {
    s"WHERE ${convert(result.conditions)}"
  }

  private def convert(conditions: Seq[Condition]): String = {
    conditions.map(convert).mkString(" todo ")
  }

  private def convert(condition: Condition): String = {
    condition match {
      case c: ComparisonCondition => s"${c.col.value} ${c.operator.value} ${convert(c.value)}"
      case c: InCondition => s"${c.col.value} ${keyWithNot(c)} ${convert(c.values)}"
      case c: BetweenCondition => s"${c.col.value} ${keyWithNot(c)} ${convert(c.value1)} AND ${convert(c.value2)}"
      case c: IsCondition => s"${c.col.value} ${keyWithNot(c)} NULL"
      case c: PluralCondition => s"${c.col.value} ${c.operator.value} ${convert(c.keyword)} ${convert(c.values)}"
    }
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
