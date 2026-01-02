case class City(
  id: Int,
  name: String,
  countryCode: String,
  district: String,
  population: Int
)
object City extends SimpleTable[City]