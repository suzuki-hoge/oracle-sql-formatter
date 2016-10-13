package formatter

import parser.{Asterisk, Columns, SelectColumns, SelectResult}

object SelectFormatter extends WhereFormatter {
  def convert(result: SelectResult): String = {
    result.where match {
      case Some(where) => s"SELECT ${convert(result.cols)} FROM ${convert(result.table)} ${convert(where)};"
      case None => s"SELECT ${convert(result.cols)} FROM ${convert(result.table)};"
    }
  }

  def convert(cols: SelectColumns): String = {
    cols match {
      case cs: Asterisk => "*"
      case cs: Columns => cs.option match {
        case Some(option) => s"${convert(option)} ${convert(cs.names)}"
        case None => convert(cs.names)
      }
    }
  }
}
