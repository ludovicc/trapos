package whitewerx.com.trapos.gateway

import org.jboss.netty.channel.ChannelHandlerContext
import org.jboss.netty.channel.ExceptionEvent
import org.jboss.netty.channel.MessageEvent
import org.jboss.netty.channel.SimpleChannelUpstreamHandler
import whitewerx.com.trapos.ShutdownListener

/**
 * The handler determines what to do with each text line received. In all cases
 * but the stop case it passes on the line to the {@link TextMessageSubscriber}.
 *
 * See: {@link TextMessageSubscriber}
 * See: Special Stop Command SHUTDOWN_COMMAND
 *
 * @author ewhite
 */
object TextMessageHandler {

  val (logger, formatter) = ZeroLoggerFactory.newLogger(this)

  /**The special shutdown command */
  val SHUTDOWN_COMMAND = "C|STOP"

}

class TextMessageHandler(private val textMessageSubscriber: TextMessageSubscriber, private val shutdownListener: ShutdownListener) extends SimpleChannelUpstreamHandler {

  import TextMessageHandler.{logger, SHUTDOWN_COMMAND}
  import TextMessageHandler.formatter._

  /**
   * At this point the message will be lined delimited and these messages can
   * be directly sent to disruptor for processing.
   */
  override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) {
    val delimitedMessage: String = e.getMessage.asInstanceOf[String].trim

    delimitedMessage match {
      case "" => return
      case SHUTDOWN_COMMAND =>
        logger.finest(delimitedMessage)
        shutdownListener.notifyShutdown
      case _ =>
        logger.finest(delimitedMessage)
        publish(delimitedMessage)
    }

  }

  /**
   * Finally send the message to the subscriber.
   *
   * @param delimitedMessage
   */
  private def publish(delimitedMessage: String) {
    textMessageSubscriber.accept(delimitedMessage)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, e: ExceptionEvent) {
    logger.severe { _ ++= "Unexpected exception in the text message gateway. Closing the channel." ++= e.toString }
    e.getChannel.close
    shutdownListener.notifyShutdown
  }

}
