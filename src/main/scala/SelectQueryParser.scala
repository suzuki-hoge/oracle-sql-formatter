case class SelectResult(cols: SelectColumns, tableName: Table, where: Option[WhereResult])

trait SelectColumns

case class Asterisk(value: String) extends SelectColumns

case class Columns(option: Option[Keyword], names: Cols) extends SelectColumns

object SelectQueryParser extends OracleParser with WhereCondition {
  def selectQuery = select ~> selectColumns ~ (from ~> table) ~ opt(whereCondition) <~ ";" ^^ {
    case cols ~ table ~ where => SelectResult(cols, table, where)
  }

  def select = keyword("SELECT")

  def selectColumns = asterisk | columns

  def asterisk = "*" ^^ Asterisk

  def columns = opt(all | distinct) ~ cols ^^ { case option ~ cols => Columns(option, cols)}

  def from = keyword("FROM")

  def distinct = keyword("DISTINCT")


  def apply(input: String): Either[String, Any] = parseAll(selectQuery, input) match {
    case Success(result, next) => Right(result)
    case NoSuccess(error, next) => Left(s"error on line ${next.pos.line}")
  }
}
