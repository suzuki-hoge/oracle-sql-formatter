package formatter

import org.scalatest.FunSuite
import parser._

class SelectFormatterTest extends FunSuite {

  val indent = new Indent
  test("select missing where") {
    Indent.init("  ")
    assert(
      SelectFormatter.convert(
        SelectResult(Asterisk("*"), Table("books"), None)
      )(indent) ==
        """SELECT
          |  *
          |FROM
          |  books
          |;""".stripMargin
    )

    Indent.init("  ")
    assert(
      SelectFormatter.convert(
        SelectResult(Columns(None, Cols(Col("foo"), Col("bar"))), Table("BOOKS"), None)
      )(indent) ==
        """SELECT
          |  foo, bar
          |FROM
          |  books
          |;""".stripMargin
    )

    Indent.init("  ")
    assert(
      SelectFormatter.convert(
        SelectResult(Columns(Option(Keyword("distinct")), Cols(Col("foo"), Col("bar"))), Table("BOOKS"), None)
      )(indent) ==
        """SELECT DISTINCT
          |  foo, bar
          |FROM
          |  books
          |;""".stripMargin
    )
  }

  test("select") {
    val where = WhereResult(
      Conditions(
        ComparisonCondition(Col("name"), Operator("="), StringValue("foo")),
        (Keyword("and"), BetweenCondition(Col("rate"), Option(Keyword("not")), IntValue("1000"), IntValue("2000")))
      )
    )

    Indent.init("  ")
    assert(
      SelectFormatter.convert(
        SelectResult(Asterisk("*"), Table("books"), Option(where))
      )(indent) ==
        """SELECT
          |  *
          |FROM
          |  books
          |WHERE
          |  name = 'foo'
          |  AND rate NOT BETWEEN 1000 AND 2000
          |;""".stripMargin
    )
  }
}

