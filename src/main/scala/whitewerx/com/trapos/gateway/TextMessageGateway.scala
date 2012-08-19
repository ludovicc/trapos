package whitewerx.com.trapos.gateway

import java.net.InetSocketAddress
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.channel.Channel
import org.jboss.netty.channel.ChannelFactory
import org.jboss.netty.channel.ChannelPipeline
import org.jboss.netty.channel.ChannelPipelineFactory
import org.jboss.netty.channel.Channels
import org.jboss.netty.channel.group.ChannelGroup
import org.jboss.netty.channel.group.ChannelGroupFuture
import org.jboss.netty.channel.group.DefaultChannelGroup
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder
import org.jboss.netty.handler.codec.frame.Delimiters
import org.jboss.netty.handler.codec.string.StringDecoder
import org.jboss.netty.util.CharsetUtil
import whitewerx.com.trapos.ShutdownListener

/**
 * This is a text gateway that accepts messages line by line.
 *
 * It is implemented using Netty.
 *
 * @author ewhite
 */
object TextMessageGateway {

  val (logger, formatter) = ZeroLoggerFactory.newLogger(this)

  val TCPIP_PORT: Int = 7000
  val TCPIP_INTERFACE = "0.0.0.0"

  /**
   * This must match the producing claim strategy.
   */
  var PUBLISHING_THREADS: Int = 1

  /**
   * The maximum line length of any text message, this is used in the framing
   * of the messages as they are received over the TCP/IP socket.
   */
  private val MAX_LINE_LENGTH: Int = 500
}

class TextMessageGateway(/** This receiver is interested in the text messages (e.g. Disruptor Producer). */
                         private val textMessageSubscriber: TextMessageSubscriber,
                         private val shutdownListener: ShutdownListener)
    extends Runnable with ShutdownListener {

  import TextMessageGateway.{logger, TCPIP_INTERFACE, TCPIP_PORT, PUBLISHING_THREADS, MAX_LINE_LENGTH}
  import TextMessageGateway.formatter._

  /**
   * Starts the Netty server up and waits for a shutdown message to come
   * through the gateway.
   */
  def run {
    val bootstrap: ServerBootstrap = createServer
    configureTextMessageProcessingPipeline(bootstrap)
    configureTCPIPSettings(bootstrap)
    startServer(bootstrap)
    waitForShutdown
  }

  /**
   * Creates the server with its thread pools.
   */
  private def createServer: ServerBootstrap = { new ServerBootstrap(factory) }

  /**
   * Starts the server listening on the TCP/IP socket.
   * @param bootstrap
   */
  private def startServer(bootstrap: ServerBootstrap) {
    val gateway: Channel = bootstrap.bind(new InetSocketAddress(TCPIP_INTERFACE, TCPIP_PORT))
    allChannels.add(gateway)
    logger.info{ _ ++= "Started the gateway. " ++= TCPIP_INTERFACE ++= ":" ++= TCPIP_PORT.toString }
  }

  def notifyShutdown {
    shutdown.countDown()
  }

  private def configureTCPIPSettings(bootstrap: ServerBootstrap) {
    bootstrap.setOption("child.tcpNoDelay", true)
    bootstrap.setOption("child.keepAlive", true)
  }

  private def waitForShutdown() {
    try {
      shutdown.await()
    }
    catch {
      case e: InterruptedException => {
        logger.info("Gateway interupted, waiting for shutdown.", e)
      }
    }
    handleShutdown()
  }

  /**
   * Sets up a pipline that delimits lines based on CRLF/LF and a line length
   * no greater than MAX_LINE_LENGTH.
   *
   * @param bootstrap
   */
  private def configureTextMessageProcessingPipeline(bootstrap: ServerBootstrap) {
    val gatewayShutdownListener: ShutdownListener = this
    bootstrap.setPipelineFactory(new ChannelPipelineFactory {
      def getPipeline: ChannelPipeline = {
        val pipeline: ChannelPipeline = Channels.pipeline
        pipeline.addLast("Framer", new DelimiterBasedFrameDecoder(MAX_LINE_LENGTH, Delimiters.lineDelimiter : _*))
        pipeline.addLast("Decoder", new StringDecoder(CharsetUtil.UTF_8))
        pipeline.addLast("Gateway", new TextMessageHandler(textMessageSubscriber, gatewayShutdownListener))
        pipeline
      }
    })
  }

  /**
   * Properly stops the gateway releasing resources.
   */
  private def handleShutdown() {
    try {
      logger.info("Stopping the gateway.")
      val shutdown: ChannelGroupFuture = allChannels.close
      shutdown.awaitUninterruptibly
      factory.releaseExternalResources()
      logger.info("Stopped the gateway.")
    }
    finally {
      shutdownListener.notifyShutdown
    }
  }

  private val shutdown: CountDownLatch = new CountDownLatch(1)
  private val allChannels: ChannelGroup = new DefaultChannelGroup("tm-gateway")
  private lazy val factory: ChannelFactory = {
    val boss: ExecutorService = Executors.newCachedThreadPool
    val workers: ExecutorService = Executors.newFixedThreadPool(PUBLISHING_THREADS)
    new NioServerSocketChannelFactory(boss, workers, PUBLISHING_THREADS)
  }
}

