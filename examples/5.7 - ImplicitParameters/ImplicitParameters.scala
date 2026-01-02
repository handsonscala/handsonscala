def main() =
  class Foo(val value: Int)

  def bar(using foo: Foo) = foo.value + 10

  given foo: Foo = Foo(1)

  assert(bar == 11) // `foo` is resolved implicitly

  assert(bar(using foo) == 11) // passing in `foo` explicitly

  import scala.concurrent.*
  trait Employee
  trait Role
  case class EmployeeWithRole(e: Employee, r: Role)
  def getEmployee(id: Int)(using ec: ExecutionContext): Future[Employee] = ???
  def getRole(employee: Employee)(using ec: ExecutionContext): Future[Role] = ???

  given executionContext: ExecutionContext = ???

  def bigEmployee: Future[EmployeeWithRole] =
    getEmployee(100).flatMap(e => getRole(e).map(r => EmployeeWithRole(e, r)))
