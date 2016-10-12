import org.scalatest.FunSuite

class SelectQueryParserTest extends FunSuite {
  test("valid select query missing where") {
    assert(
      SelectQueryParser("SELECT * FROM books;").right.get == SelectResult(
        Asterisk("*"),
        Table("books"),
        None
      )
    )

    assert(
      SelectQueryParser("SELECT id, name FROM books;").right.get == SelectResult(
        Columns(None, Cols(List("id", "name"))),
        Table("books"),
        None
      )
    )

    assert(
      SelectQueryParser("SELECT all id, name FROM books;").right.get == SelectResult(
        Columns(Some(Keyword("all")), Cols(List("id", "name"))),
        Table("books"),
        None
      )
    )
  }

  test("valid select query") {
    assert(
      SelectQueryParser("SELECT * FROM books WHERE name = 'foo' AND job IS NOT NULL;").right.get == SelectResult(
        Asterisk("*"),
        Table("books"),
        Some(WhereResult(
          Seq(
            ComparisonCondition(Col("name"), Operator("="), Value("foo")),
            IsCondition(Col("job"), Some(Keyword("not")))
          )
        ))
      )
    )
  }

  test("invalid") {
    assert(
      SelectQueryParser("SELECT * FROM;") == Left("error on line 1")
    )
  }
}
