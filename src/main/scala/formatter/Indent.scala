package formatter

object Indent {
  var value: String = ""
  var current: String = ""

  def init(value: String): Unit = {
    this.value = value
    this.current = ""
  }

  def ++(): Unit = {
    current = current + value
  }

  def --(): Unit = {
    current = current.drop(value.size)
  }

  override def toString: String = {
    current
  }
}
