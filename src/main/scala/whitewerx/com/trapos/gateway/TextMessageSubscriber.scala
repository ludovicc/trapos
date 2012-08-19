package whitewerx.com.trapos.gateway

/**
 * The implementor will receive the text message on a Netty
 * thread as they arrive.
 *
 * @author ewhite
 */
trait TextMessageSubscriber {
  /**
   * @param delimitedMessage
   */
  def accept(delimitedMessage: String)
}

