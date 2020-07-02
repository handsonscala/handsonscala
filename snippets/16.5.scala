abstract class StateMachineActor[T]()(implicit cc: Context) extends Actor[T] {
  class State(val run: T => State)
  protected[this] def initialState: State
}
