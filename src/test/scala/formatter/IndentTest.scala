package formatter

import org.scalatest.FunSuite

class IndentTest extends FunSuite {
  test("indent with 2 spaces") {
    val tab2 = new Indent
    assert(s"${tab2}" == "")

    assert(s"${tab2.inc}" == "  ")

    assert(s"${tab2.inc.dec}" == "")
  }

  test("indent with 4 spaces") {
    val tab4 = new Indent(size = 4)
    assert(s"${tab4}" == "")

    assert(s"${tab4.inc}" == "    ")

    assert(s"${tab4.inc.dec}" == "")
  }

  test("indent with initial depth") {
    val tab2 = new Indent(depth = 1)
    assert(s"${tab2}" == "  ")
  }
}

