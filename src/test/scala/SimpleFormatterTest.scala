import formatter.Indent
import org.scalatest.FunSuite

class SimpleFormatterTest extends FunSuite {
  test("select re-format") {
    Indent.init("  ")
    assert(
      SimpleFormatter.select(
        """select * from books where name = 'foo';
        """.stripMargin
      ).right.get ==
        """SELECT
          |  *
          |FROM
          |  books
          |WHERE
          |  name = 'foo'
          |;""".stripMargin
    )

    Indent.init("  ")
    assert(
      SimpleFormatter.select(
        """select all id, name
          |from books
          |where name = 'foo'
          |and genre in ('foo', 'bar');
        """.stripMargin
      ).right.get ==
        """SELECT ALL
          |  id, name
          |FROM
          |  books
          |WHERE
          |  name = 'foo'
          |  AND genre IN (
          |    'foo'
          |    , 'bar'
          |  )
          |;""".stripMargin
    )

    Indent.init("  ")
    assert(
      SimpleFormatter.select(
        """select * from books where;
        """.stripMargin
      ).left.get == "error on line 1"
    )

    Indent.init("  ")
    assert(
      SimpleFormatter.select(
        """select *
          |from books
          |where;
        """.stripMargin
      ).left.get == "error on line 3"
    )
  }
}

