> var total = 0

> for i <- Range(0, 10) do
    total += (if i % 2 == 0 then i else 2)

> total
res77: Int = 30
