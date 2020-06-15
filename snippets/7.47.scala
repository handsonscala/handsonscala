os.proc(command: os.Shellable*)
  .spawn(cwd: Path = null,
         env: Map[String, String] = null,
         stdin: os.ProcessInput = os.Pipe,
         stdout: os.ProcessOutput = os.Pipe,
         stderr: os.ProcessOutput = os.Inherit,
         mergeErrIntoOut: Boolean = false,
         propagateEnv: Boolean = true): os.SubProcess
