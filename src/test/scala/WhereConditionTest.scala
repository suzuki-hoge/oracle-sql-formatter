import org.scalatest.FunSuite

class WhereConditionTest extends FunSuite with ParserAssert {

  object Parser extends WhereCondition

  test("valid condition") {
    val inputs = List(
      "name = 'foo'",

      "name IN ('foo', 'bar')",
      "name NOT IN ('foo', 'bar')",

      "rate BETWEEN 1000 AND 2000",
      "rate NOT BETWEEN 1000 AND 2000",

      "job IS NULL",
      "job IS NOT NULL",

      "location = ANY ('foo', 'bar')",
      "job IS NOT NULL"
    )

    assertAllFalse(
      inputs.map(Parser.parseAll(Parser.condition, _).isEmpty)
    )
  }

  test("valid conditions") {
    val inputs = List(
      "name = 'foo' AND job IS NOT NULL",

      "name IN ('foo', 'bar') OR rate BETWEEN 1000 AND 2000"
    )

    assertAllFalse(
      inputs.map(Parser.parseAll(Parser.conditions, _).isEmpty)
    )
  }

  test("valid where condition") {
    val inputs = List(
      "WHERE name = 'foo' AND job IS NOT NULL"
    )

    assertAllFalse(
      inputs.map(Parser.parseAll(Parser.whereCondition, _).isEmpty)
    )
  }
}

