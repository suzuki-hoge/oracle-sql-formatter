package formatter

import parser.{Asterisk, Columns, SelectColumns, SelectResult}

object SelectFormatter extends WhereFormatter {
  def convert(result: SelectResult): String = {
    result.where match {
      case Some(where) => s"SELECT${convert(result.cols)}\nFROM\n${convert(result.table)}\n${convert(where)}\n;"
      case None => s"SELECT${convert(result.cols)}\nFROM\n${convert(result.table)}\n;"
    }
  }

  private def convert(cols: SelectColumns): String = {
    Indent.++()

    val s = cols match {
      case cs: Asterisk => s"\n${Indent.current}*"
      case cs: Columns => cs.option match {
        case Some(option) => s" ${convert(option)}\n${Indent.current}${convert(cs.names)}"
        case None => s"\n${Indent.current}${convert(cs.names)}"
      }
    }

    Indent.--()

    s
  }
}
