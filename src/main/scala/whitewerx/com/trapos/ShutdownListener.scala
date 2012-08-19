package whitewerx.com.trapos

/**
 * Used by the gateway to terminate the system on the special
 * command
 * <pre>
 * C|STOP
 * </pre>
 * @author ewhite
 *
 */
trait ShutdownListener {
  def notifyShutdown
}
