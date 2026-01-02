pg> \d country
| Column         | Type           | Modifiers |
|----------------+----------------+-----------|
| code           | character(3)   |  not null |
| name           | character vary |  not null |
| continent      | character vary |  not null |
| region         | character vary |  not null |
| surfacearea    | real           |  not null |
| indepyear      | smallint       |           |
| population     | integer        |  not null |
| lifeexpectancy | real           |           |
| gnp            | numeric(10,2)  |           |
| gnpold         | numeric(10,2)  |           |
| localname      | character vary |  not null |
| governmentform | character vary |  not null |
| headofstate    | character vary |           |
| capital        | integer        |           |
| code2          | character(2)   |  not null |