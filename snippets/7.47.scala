os.spawn(cmd: os.Shellable,
         cwd: Path = null,
         env: Map[String, String] = null,
         stdin: os.ProcessInput = os.Pipe,
         stdout: os.ProcessOutput = os.Pipe,
         stderr: os.ProcessOutput = os.Inherit,
         mergeErrIntoOut: Boolean = false,
         shutdownGracePeriod: Long = 100,
         destroyOnExit: Boolean = true): os.SubProcess
