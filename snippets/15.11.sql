pg> \d countrylanguage
| Column      | Type           | Modifiers |
|-------------+----------------+-----------|
| countrycode | character(3)   |  not null |
| language    | character vary |  not null |
| isofficial  | boolean        |  not null |
| percentage  | real           |  not null |