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

class Indent(depth:Int = 0, size:Int = 2) {
  private[this] final val SPACE = " "
  override def toString:String =  SPACE * depth*size

  def inc:Indent = new Indent(depth+1, size)
  def dec:Indent = new Indent(depth-1, size)
}