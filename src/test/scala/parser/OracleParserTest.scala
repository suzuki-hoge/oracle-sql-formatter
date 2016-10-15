package parser

import org.scalatest.FunSuite

class OracleParserTest extends FunSuite {

  object Parser extends OracleParser {
    def _keyword(arg: String, s: String) = parseAll(keyword(arg), s)

    def _col(s: String) = parseAll(col, s)

    def _cols(s: String) = parseAll(cols, s)

    def _table(s: String) = parseAll(table, s)
  }


  test("valid keyword") {
    assert(
      Parser._keyword("foo", "foo").get == Keyword("foo")
    )
  }

  test("valid col") {
    assert(
      Parser._col("a_B-1").get == Col("a_B-1")
    )

    assert(
      Parser._col("").isEmpty
    )

    assert(
      Parser._col("0000011111222223333344444555556").isEmpty
    )

    assert(
      Parser._col("a b").isEmpty
    )
  }

  test("valid cols") {
    assert(
      Parser._cols("foo, bar").get == Cols(Col("foo"), Col("bar"))
    )
  }

  test("valid table") {
    assert(
      Parser._table("books").get == Table("books")
    )
  }
}

