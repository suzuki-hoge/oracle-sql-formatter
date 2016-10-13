package formatter

import parser._

trait OracleFormatter {
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
    Indent.++()
    val s = s"${Indent.current}${table.value.toLowerCase}"
    Indent.--()
    s
  }

  def convert(value: Value): String = {
    value match {
      case v: StringValue => s"'${v.value}'"
      case v: IntValue => v.value
    }
  }

  def convert(values: Values): String = {
    var result: String = "(\n"
    Indent.++()
    result += Indent.current

    val s: String = values match {
      case vs: StringValues => s"${vs.values.map(convert).mkString(s"\n${Indent.current}, ")}"
      case vs: IntValues => s"${vs.values.map(convert).mkString(s"\n${Indent.current}, ")}"
    }

    result += s

    Indent.--()
    result += s"\n${Indent.current})"

    result
  }
}
