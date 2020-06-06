
class Foo(val value: Int)

def bar(implicit foo: Foo) = foo.value + 10

implicit val foo: Foo = new Foo(1)

assert(bar == 11) // `foo` is resolved implicitly

assert(bar(foo) == 11) // passing in `foo` explicitly

import scala.concurrent._
trait Employee
trait Role
case class EmployeeWithRole(e: Employee, r: Role)
def getEmployee(id: Int)(implicit ec: ExecutionContext): Future[Employee] = ???
def getRole(employee: Employee)(implicit ec: ExecutionContext): Future[Role] = ???

implicit def executionContext: ExecutionContext = ???

def bigEmployee: Future[EmployeeWithRole] = {
  getEmployee(100).flatMap(e =>
    getRole(e).map(r =>
      EmployeeWithRole(e, r)
    )
  )
}
