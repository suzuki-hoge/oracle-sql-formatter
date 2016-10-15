package formatter

import org.scalatest.FunSuite

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

