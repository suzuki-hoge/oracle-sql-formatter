package formatter

import parser._

object ColumnsOf extends OracleFormatter {
  def unapply(c: Columns): Option[(String, String)] = c match {
    case Columns(option, names) => Some(option.fold("")(" " + convert(_)), convert(names))
    case _ => None
  }
}

object SelectResultOf extends WhereFormatter {
  def unapply(r: SelectResult): Option[(String, String, String)] = r match {
    case SelectResult(cols, table, where) => Some(convert(cols), convert(table), where.fold("")(convert(_) + "\n"))
    case _ => None
  }

  private def convert(cols: SelectColumns): String = {
    >>((cols: SelectColumns) => cols match {
      case _: Asterisk => s"\n${__}*"
      case ColumnsOf(opt, names) => s"$opt\n${__}$names"
    }, cols)
  }
}

object SelectFormatter extends WhereFormatter {
  def convert(result: SelectResult): String = result match {
    case SelectResultOf(cols, table, where) => s"SELECT$cols\nFROM\n$table\n$where;"
  }
}
