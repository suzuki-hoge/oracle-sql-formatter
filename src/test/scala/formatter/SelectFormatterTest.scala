package formatter

import org.scalatest.FunSuite
import parser._

class SelectFormatterTest extends FunSuite {

  test("select missing where") {
    assert(
      SelectFormatter.convert(
        SelectResult(Asterisk("*"), Table("books"), None)
      ) == "SELECT * FROM books;"
    )

    assert(
      SelectFormatter.convert(
        SelectResult(Columns(None, Cols(Seq(Col("foo"), Col("bar")))), Table("BOOKS"), None)
      ) == "SELECT foo, bar FROM books;"
    )

    assert(
      SelectFormatter.convert(
        SelectResult(Columns(Option(Keyword("distinct")), Cols(Seq(Col("foo"), Col("bar")))), Table("BOOKS"), None)
      ) == "SELECT DISTINCT foo, bar FROM books;"
    )
  }

  test("select") {
    val where = WhereResult(
      Conditions(
        ComparisonCondition(Col("name"), Operator("="), StringValue("foo")),
        Seq(
          (Keyword("and"), BetweenCondition(Col("rate"), Option(Keyword("not")), IntValue("1000"), IntValue("2000")))
        )
      )
    )

    assert(
      SelectFormatter.convert(
        SelectResult(Asterisk("*"), Table("books"), Option(where))
      ) == "SELECT * FROM books WHERE name = 'foo' AND rate NOT BETWEEN 1000 AND 2000;"
    )
  }
}

