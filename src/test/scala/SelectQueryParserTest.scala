import org.scalatest.FunSuite

class SelectQueryParserTest extends FunSuite with ParserAssert {
  test("valid select query missing where") {
    val inputs = List(
      "SELECT * FROM books;",
      "SELECT id, name FROM books;",
      "SELECT ALL id, name FROM books;",
      "SELECT book_id, book-name FROM books;"
    )

    assertAllFalse(
      inputs.map(SelectQueryParser(_).isEmpty)
    )
  }

  test("invalid select query missing where") {
    val inputs = List(
      "SELECT ALL DISTINCT id, name FROM books;"
    )

    assertAllTrue(
      inputs.map(SelectQueryParser(_).isEmpty)
    )
  }

  test("valid select query") {
    val inputs = List(
      "SELECT * FROM books WHERE name = 'foo';",
      "SELECT DISTINCT id, name FROM books WHERE name = 'foo' OR id BETWEEN 1 AND 5;"
    )

    assertAllFalse(
      inputs.map(SelectQueryParser(_).isEmpty)
    )
  }

  test("invalid select query") {
    val inputs = List(
      "SELECT * FROM books WHERE;"
    )

    assertAllTrue(
      inputs.map(SelectQueryParser(_).isEmpty)
    )
  }
}
