import org.scalatest.FunSuite

class OracleParserTest extends FunSuite with ParserAssert {

  object Parser extends OracleParser {
    def _keyword = keyword("foo")

    def _rec = rec("foo")

    def _brc = brc("foo")
  }

  test("valid validName") {
    val inputs = List(
      "abc", "ABC", "012", "_", "-", "a_A-0", "-_"
    )

    assertAllFalse(
      inputs.map(Parser.parseAll(Parser.validName, _).isEmpty)
    )
  }

  test("invalid validName") {
    val inputs = List(
      "", "0000011111222223333344444555556", "foo.com", "bar@com"
    )

    assertAllTrue(
      inputs.map(Parser.parseAll(Parser.validName, _).isEmpty)
    )
  }

  test("valid value") {
    val inputs = List(
      "'abc'", "'ABC'", "123", "890"
    )

    assertAllFalse(
      inputs.map(Parser.parseAll(Parser.value, _).isEmpty)
    )
  }

  test("invalid values") {
    val inputs = List(
      "'ab12'", "'a_b'", "012"
    )

    assertAllTrue(
      inputs.map(Parser.parseAll(Parser.value, _).isEmpty)
    )
  }

  test("valid keyword") {
    val inputs = List(
      "foo", "FOO"
    )

    assertAllFalse(
      inputs.map(Parser.parseAll(Parser._keyword, _).isEmpty)
    )
  }

  test("valid recursive") {
    val inputs = List(
      "foo", "foo,foo", "foo, foo", "foo,  foo"
    )

    assertAllFalse(
      inputs.map(Parser.parseAll(Parser._rec, _).isEmpty)
    )
  }


  test("valid brackets") {
    val inputs = List(
      "(foo)"
    )

    assertAllFalse(
      inputs.map(Parser.parseAll(Parser._brc, _).isEmpty)
    )
  }
}

