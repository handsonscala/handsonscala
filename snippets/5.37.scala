def getEmployee(id: Int)(implicit ec: ExecutionContext): Future[Employee] = ...
def getRole(employee: Employee)(implicit ec: ExecutionContext): Future[Role] = ...

implicit val executionContext: ExecutionContext = ...

val bigEmployee: Future[EmployeeWithRole] = {
  getEmployee(100).flatMap(e =>
    getRole(e).map(r =>
      EmployeeWithRole(e, r)
    )
  )
}
