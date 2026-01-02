//| mvnDeps:
//| - com.lihaoyi::scalasql-simple:0.2.7
//%drop-lines 0,3
import scalasql.simple.SimpleTable
case class Country(
  code: String,
  name: String,
  continent: String,
  region: String,
  surfaceArea: Double,
  indepYear: Option[Int],
  population: Int,
  lifeExpectancy: Option[Double],
  gnp: Option[math.BigDecimal],
  gnpold: Option[math.BigDecimal],
  localName: String,
  governmentForm: String,
  headOfState: Option[String],
  capital: Option[Int],
  code2: String
)
object Country extends SimpleTable[Country]
