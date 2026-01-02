//| mvnDeps:
//| - com.lihaoyi::scalasql-simple:0.2.7
//%drop-lines 0,3
import scalasql.simple.SimpleTable
case class City(
  id: Int,
  name: String,
  countryCode: String,
  district: String,
  population: Int
)
object City extends SimpleTable[City]
