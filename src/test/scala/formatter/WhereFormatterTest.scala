package formatter

import org.scalatest.FunSuite
import parser._

class WhereFormatterTest extends FunSuite {

  object Formatter extends WhereFormatter

  val not = Some(Keyword("not"))

  def create(conditions: Condition*): WhereResult = WhereResult(conditions)

  test("condition") {
    assert(
      Formatter.convert(
        create(ComparisonCondition(Col("name"), Operator("="), StringValue("foo")))
      ) == "WHERE name = 'foo'"
    )

    assert(
      Formatter.convert(
        create(InCondition(Col("name"), None, StringValues(Seq(StringValue("foo"), StringValue("bar")))))
      ) == "WHERE name IN ('foo', 'bar')"
    )

    assert(
      Formatter.convert(
        create(BetweenCondition(Col("rate"), not, IntValue("1000"), IntValue("2000")))
      ) == "WHERE rate NOT BETWEEN 1000 AND 2000"
    )

    assert(
      Formatter.convert(
        create(IsCondition(Col("job"), not))
      ) == "WHERE job IS NOT NULL"
    )

    assert(
      Formatter.convert(
        create(PluralCondition(Col("location"), Operator("="), Keyword("any"), StringValues(Seq(StringValue("foo"), StringValue("bar")))))
      ) == "WHERE location = ANY ('foo', 'bar')"
    )
  }

  test("conditions") {
    assert(
      Formatter.convert(
        create(
          ComparisonCondition(Col("name"), Operator("="), StringValue("foo")),
          BetweenCondition(Col("rate"), not, IntValue("1000"), IntValue("2000"))
        )
      ) == "WHERE name = 'foo' todo rate NOT BETWEEN 1000 AND 2000"
    )
  }
}

