package formatter

import parser._

trait OracleFormatter {
  def __ = Indent.current

  def >>[T](f: T => String, arg: T): String = {
    Indent.++()
    val s = f(arg)
    Indent.--()
    s
  }

  def brc[T](f: T => String, arg: T): String = {
    var s = "(\n"
    Indent.++()
    s += f(arg)
    Indent.--()
    s += s"\n${__})"

    s
  }

  def convert(col: Col): String = {
    col.value.toLowerCase
  }

  def convert(cols: Cols): String = {
    cols.values.map(convert).mkString(", ")
  }

  def convert(keyword: Keyword): String = {
    keyword.value.toUpperCase
  }

  def convert(table: Table): String = {
    >>((table: Table) => s"${__}${table.value.toLowerCase}", table)
  }

  def convert(value: Value): String = {
    value match {
      case v: StringValue => s"'${v.value}'"
      case v: IntValue => v.value
    }
  }

  def convert(values: Values): String = {
    brc((values: Values) => s"${__}${values.values.map(convert).mkString(s"\n${__}, ")}"
      , values)
  }
}
