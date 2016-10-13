package parser

import org.scalatest.FunSuite

class SelectParserTest extends FunSuite {
  test("valid select query missing where") {
    assert(
      SelectParser("SELECT * FROM books;").right.get == SelectResult(
        Asterisk("*"),
        Table("books"),
        None
      )
    )

    assert(
      SelectParser("SELECT id, name FROM books;").right.get == SelectResult(
        Columns(None, Cols(Seq(Col("id"), Col("name")))),
        Table("books"),
        None
      )
    )

    assert(
      SelectParser("SELECT all id, name FROM books;").right.get == SelectResult(
        Columns(Some(Keyword("all")), Cols(Seq(Col("id"), Col("name")))),
        Table("books"),
        None
      )
    )
  }

  test("valid select query") {
    assert(
      SelectParser("SELECT * FROM books WHERE name = 'foo' AND job IS NOT NULL;").right.get == SelectResult(
        Asterisk("*"),
        Table("books"),
        Some(WhereResult(
          Seq(
            ComparisonCondition(Col("name"), Operator("="), StringValue("foo")),
            IsCondition(Col("job"), Some(Keyword("not")))
          )
        ))
      )
    )
  }

  test("invalid") {
    assert(
      SelectParser("SELECT * FROM;") == Left("error on line 1")
    )
  }
}
