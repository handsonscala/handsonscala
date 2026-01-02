case class CountryLanguage(
  countrycode: String,
  language: String,
  isOfficial: Boolean,
  percentage: Double
)
object CountryLanguage
extends SimpleTable[CountryLanguage]