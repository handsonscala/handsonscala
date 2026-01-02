//| mvnDeps:
//| - com.lihaoyi::scalasql-simple:0.2.7
//%drop-lines 0,3
import scalasql.simple.SimpleTable
case class CountryLanguage(
  countrycode: String,
  language: String,
  isOfficial: Boolean,
  percentage: Double
)
object CountryLanguage
extends SimpleTable[CountryLanguage]
