package formatter

import org.scalatest.FunSuite
import parser._

class IndentTest extends FunSuite {
  test("indent") {
    Indent.init("  ")
    assert(s"$Indent" == "")

    Indent.++()
    assert(s"$Indent" == "  ")

    Indent.++()
    assert(s"$Indent" == "    ")

    Indent.--()
    assert(s"$Indent" == "  ")

    Indent.--()
    assert(s"$Indent" == "")
  }
}

