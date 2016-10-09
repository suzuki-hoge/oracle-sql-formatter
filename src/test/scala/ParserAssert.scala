trait ParserAssert {
  def assertAllFalse(bs: List[Boolean]): Unit = {
    assertAll(bs, expected = false)
  }

  def assertAllTrue(bs: List[Boolean]): Unit = {
    assertAll(bs, expected = true)
  }

  def assertAll(bs: List[Boolean], expected: Boolean): Unit = {
    val res = bs.forall(_ == expected)

    if (!res) {
      println(bs)
    }

    assert(res)
  }
}
