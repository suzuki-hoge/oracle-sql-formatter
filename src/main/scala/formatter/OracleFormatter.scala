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
    table.value.toLowerCase
  }

  def convert(value: Value): String = {
    value match {
      case v: StringValue => s"'${v.value}'"
      case v: IntValue => v.value
    }
  }

  def convert(values: Values): String = {
    values match {
      case vs: StringValues => s"(${vs.values.map(convert).mkString(", ")})"
      case vs: IntValues => s"(${vs.values.map(convert).mkString(", ")})"
    }
  }
}
