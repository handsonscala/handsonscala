@ import $ivy.`at.favre.lib:bcrypt:0.9.0`

@ import at.favre.lib.crypto.bcrypt.{BCrypt, LongPasswordStrategies}

@ val hasher = BCrypt.`with`(LongPasswordStrategies.hashSha512(BCrypt.Version.VERSION_2A))

@ def hash(name: String) = {
    val salt = name.take(16).padTo(16, ' ').getBytes
    val bytes = hasher.hash(/*difficulty*/ 17, salt, os.read.bytes(os.pwd / name))
    new String(bytes)
  }
