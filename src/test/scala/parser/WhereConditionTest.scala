package parser

import org.scalatest.FunSuite

class WhereConditionTest extends FunSuite {

  object Parser extends WhereCondition

  val not = Some(Keyword("not"))

  test("valid condition") {
    assert(
      Parser.parseAll(Parser.condition, "name = 'foo'").get == ComparisonCondition(
        Col("name"), Operator("="), StringValue("foo")
      )
    )

    assert(
      Parser.parseAll(Parser.condition, "name IN ('foo', 'bar')").get == InCondition(
        Col("name"), None, StringValues(Seq(StringValue("foo"), StringValue("bar")))
      )
    )

    assert(
      Parser.parseAll(Parser.condition, "name NOT IN ('foo', 'bar')").get == InCondition(
        Col("name"), not, StringValues(Seq(StringValue("foo"), StringValue("bar")))
      )
    )

    assert(
      Parser.parseAll(Parser.condition, "rate BETWEEN 1000 AND 2000").get == BetweenCondition(
        Col("rate"), None, IntValue("1000"), IntValue("2000")
      )
    )

    assert(
      Parser.parseAll(Parser.condition, "rate NOT BETWEEN 1000 AND 2000").get == BetweenCondition(
        Col("rate"), not, IntValue("1000"), IntValue("2000")
      )
    )

    assert(
      Parser.parseAll(Parser.condition, "job IS NULL").get == IsCondition(
        Col("job"), None
      )
    )

    assert(
      Parser.parseAll(Parser.condition, "job IS NOT NULL").get == IsCondition(
        Col("job"), not
      )
    )

    assert(
      Parser.parseAll(Parser.condition, "location = ANY ('foo', 'bar')").get == PluralCondition(
        Col("location"), Operator("="), Keyword("any"), StringValues(Seq(StringValue("foo"), StringValue("bar")))
      )
    )
  }

  test("valid where") {
    assert(
      Parser.parseAll(Parser.whereCondition, "WHERE name = 'foo' AND job IS NOT NULL").get == WhereResult(
        Seq(
          ComparisonCondition(Col("name"), Operator("="), StringValue("foo")),
          IsCondition(Col("job"), not)
        )
      )
    )
  }

  test("invalid where") {
    assert(
      Parser.parseAll(Parser.whereCondition, "WHERE name").isEmpty
    )
  }
}
