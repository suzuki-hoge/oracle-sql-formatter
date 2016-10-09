import org.scalatest.FunSuite

class SelectTest extends FunSuite {
  test("valid") {
    val inputs = List(
      "select * from books;",
      "select id, name from books;",
      "select ALL id, name from books;",
      "select book_id, book-name from books;"
    )

    inputs.map(SelectQueryParser(_).isEmpty).forall(it => true)
  }

  test("invalid") {
    val inputs = List(
      "select ALL DISTINCT id, name from books;"
    )

    inputs.map(SelectQueryParser(_).isEmpty).forall(it => false)
  }
}
