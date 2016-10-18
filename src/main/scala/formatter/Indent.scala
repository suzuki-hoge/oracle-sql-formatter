package formatter

class Indent(depth:Int = 0, size:Int = 2) {
  private[this] final val SPACE = " "
  override def toString:String =  SPACE * depth*size

  def inc:Indent = new Indent(depth+1, size)
  def dec:Indent = new Indent(depth-1, size)
}