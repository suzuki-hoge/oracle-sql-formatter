import scala.util.parsing.combinator.JavaTokenParsers

class OracleParsers extends JavaTokenParsers {
  def validName = "[a-zA-Z0-9_-]{1,30}".r

  def validNames = validName ~ rep("," ~ validName)

  def tableName = validName
}
