package formatter

import parser._

trait OracleFormatter {

  def >>(value:String)(indent:Indent):String = s"${indent.inc}${value}"

  def brc(value:String)(indent:Indent):String = s"(\n${value}\n${indent})"

  def convert(col: Col): String = {
    col.value.toLowerCase
  }

  def convert(cols: Cols): String = {
    cols.values.map(convert).mkString(", ")
  }

  def convert(keyword: Keyword): String = {
    keyword.value.toUpperCase
  }

  def convert(table: Table)(indent:Indent): String = {
    >>(table.value.toLowerCase)(indent)
  }

  def convert(value: Value): String = {
    value match {
      case v: StringValue => s"'${v.value}'"
      case v: IntValue => v.value
    }
  }

  def convert(values: Values)(indent:Indent): String = {
    brc(indent.inc.toString + values.values.map(convert).mkString(s"\n${indent.inc}, "))(indent)
  }
}
