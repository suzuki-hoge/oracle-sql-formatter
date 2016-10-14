package formatter

import parser.{Asterisk, Columns, SelectColumns, SelectResult}

object SelectFormatter extends WhereFormatter {
  def convert(result: SelectResult): String = {
    val where = result.where match {
      case Some(x) => convert(x) + "\n"
      case None => ""
    }

    s"SELECT${convert(result.cols)}\nFROM\n${convert(result.table)}\n$where;"
  }

  private def convert(cols: SelectColumns): String = {
    >>((cols: SelectColumns) => cols match {
      case cs: Asterisk => s"\n${__}*"
      case cs: Columns => cs.option match {
        case Some(option) => s" ${convert(option)}\n${__}${convert(cs.names)}"
        case None => s"\n${__}${convert(cs.names)}"
      }
    }, cols)
  }
}
