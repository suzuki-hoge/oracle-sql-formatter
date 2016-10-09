object SelectQueryParser extends OracleParser with WhereCondition {
  def selectQuery = select ~ selectColumns ~ from ~ tableName ~ opt(whereCondition) ~ ";"

  def select = keyword("SELECT")

  def selectColumns = "*" | opt(all | distinct) ~ validNames

  def from = keyword("FROM")

  override def all = keyword("ALL")

  def distinct = keyword("DISTINCT")


  def apply(input: String) = parseAll(selectQuery, input)
}
