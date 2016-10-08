package paser

object SelectQueryParser extends OracleParsers {
  def selectQuery = select ~ selectColumns ~ from ~ tableName ~ ";"

  def select = "(?i)SELECT".r

  def selectColumns = "*" | opt("ALL" | "DISTINCT") ~ validNames

  def from = "(?i)FROM".r


  def apply(input: String) = println(
    parseAll(selectQuery, input)
  )
}
