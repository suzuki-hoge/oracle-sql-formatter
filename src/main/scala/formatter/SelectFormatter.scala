package formatter

import parser._

object ColumnsOf extends OracleFormatter {
  def unapply(c: Columns): Option[(String, String)] = c match {
    case Columns(option, names) => Some(option.fold("")(" " + convert(_)), convert(names))
    case _ => None
  }
}

object SelectResultOf extends WhereFormatter {
  def unapply(r: SelectResult)(implicit indent:Indent): Option[(String, String, String)] = r match {
    case SelectResult(cols, table, where) => Some(convert(cols)(indent), convert(table)(indent), where.fold("")(convert(_)(indent) + "\n"))
    case _ => None
  }

  private def convert(cols: SelectColumns)(implicit indent:Indent): String = {
    >>(cols match {
      case _: Asterisk => s"\n${indent.inc}*"
      case ColumnsOf(opt, names) => s"$opt\n${indent.inc}$names"
    })(indent.dec)
//    >>((cols: SelectColumns) => cols match {
//      case _: Asterisk => s"\n${indent}*"
//      case ColumnsOf(opt, names) => s"$opt\n${indent}$names"
//    }, cols)
  }
}

object SelectFormatter extends WhereFormatter {
  def convert(result: SelectResult)(implicit indent:Indent): String = result match {
    case SelectResultOf(cols, table, where) => s"SELECT$cols\nFROM\n$table\n$where;"
  }
}
