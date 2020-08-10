def getEmployee(ec: ExecutionContext, id: Int): Future[Employee] = ...
def getRole(ec: ExecutionContext, employee: Employee): Future[Role] = ...

val executionContext: ExecutionContext = ...

val bigEmployee: Future[EmployeeWithRole] = {
  getEmployee(executionContext, 100).flatMap(
    executionContext,
    e =>
      getRole(executionContext, e)
        .map(executionContext, r => EmployeeWithRole(e, r))
  )
}
