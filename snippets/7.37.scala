os.proc(command: os.Shellable*)
  .call(cwd: Path = null,
        env: Map[String, String] = null,
        stdin: ProcessInput = os.Pipe,
        stdout: ProcessOutput = os.Pipe,
        stderr: ProcessOutput = os.Inherit,
        mergeErrIntoOut: Boolean = false,
        timeout: Long = Long.MaxValue,
        check: Boolean = true,
        propagateEnv: Boolean = true): os.CommandResult
