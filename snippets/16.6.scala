abstract class StateMachineActor[T]()(using cc: Context) extends Actor[T]:
  class State(val run: T => State)
  protected def initialState: State
