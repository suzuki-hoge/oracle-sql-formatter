package formatter

import parser._

trait OracleFormatter {
  def __ = Indent.current

//  val indent:Indent = new Indent

//  def >>[T](f: T => String, arg: T): String = {
//    Indent.++()
//    val s = f(arg)
//    Indent.--()
//    s
//  }

  def >>(value:String)(indent:Indent):String = s"${indent.inc}${value}"

  def brc(value:String)(indent:Indent):String = s"(\n${value}\n${indent})"

//  def brc[T](f: T => String, arg: T): String = {
//    var s = "(\n"
//    Indent.++()
//    s += f(arg)
//    Indent.--()
//    s += s"\n${__})"
//
//    s
//  }

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
//    s"${indent.inc}${table.value.toLowerCase}"
  }

  def convert(value: Value): String = {
    value match {
      case v: StringValue => s"'${v.value}'"
      case v: IntValue => v.value
    }
  }

  def convert(values: Values)(indent:Indent): String = {
    brc(indent.inc.toString + values.values.map(convert).mkString(s"\n${indent.inc}, "))(indent)
//    s"(\n${indent.inc}${values.values.map(convert).mkString(s"\n${indent.inc}, ")}\n${indent})"
  }
}
