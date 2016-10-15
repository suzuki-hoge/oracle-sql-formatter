package formatter

import org.scalatest.FunSuite
import parser._

class WhereFormatterTest extends FunSuite {

  object Formatter extends WhereFormatter

  val not = Some(Keyword("not"))

  def create(head: Condition): WhereResult = create(head, Seq.empty: _*)

  def create(head: Condition, tails: (Keyword, Condition)*): WhereResult = WhereResult(Conditions(head, tails: _*))

  test("condition") {
    Indent.init("  ")
    assert(
      Formatter.convert(
        create(ComparisonCondition(Col("name"), Operator("="), StringValue("foo")))
      ) ==
        """WHERE
          |  name = 'foo'""".stripMargin
    )

    Indent.init("  ")
    assert(
      Formatter.convert(
        create(InCondition(Col("name"), None, Values(StringValue("foo"), StringValue("bar"))))
      ) ==
        """WHERE
          |  name IN (
          |    'foo'
          |    , 'bar'
          |  )""".stripMargin
    )

    Indent.init("  ")
    assert(
      Formatter.convert(
        create(BetweenCondition(Col("rate"), not, IntValue("1000"), IntValue("2000")))
      ) ==
        """WHERE
          |  rate NOT BETWEEN 1000 AND 2000""".stripMargin
    )

    Indent.init("  ")
    assert(
      Formatter.convert(
        create(IsCondition(Col("job"), not))
      ) ==
        """WHERE
          |  job IS NOT NULL""".stripMargin
    )

    Indent.init("  ")
    assert(
      Formatter.convert(
        create(PluralCondition(Col("location"), Operator("="), Keyword("any"), Values(StringValue("foo"), StringValue("bar"))))
      ) ==
        """WHERE
          |  location = ANY (
          |    'foo'
          |    , 'bar'
          |  )""".stripMargin
    )
  }

  test("conditions") {
    Indent.init("  ")
    assert(
      Formatter.convert(
        create(
          ComparisonCondition(Col("name"), Operator("="), StringValue("foo")),
          (Keyword("and"), PluralCondition(Col("rate"), Operator("="), Keyword("any"), Values(IntValue("1000"), IntValue("2000"))))
        )
      ) ==
        """WHERE
          |  name = 'foo'
          |  AND rate = ANY (
          |    1000
          |    , 2000
          |  )""".stripMargin
    )
  }
}

