package whitewerx.com.trapos

import disruptor._
import gateway.TextMessageGateway
import java.util.concurrent.{Future => JFuture, TimeUnit, CountDownLatch, Executors, ExecutorService}
import com.lmax.disruptor._
import com.lmax.disruptor.RingBuffer
import model.{PortfolioPosition, Position}
import translators.{TradeTranslator, RateTranslator}

/**
 * @author ludo
 */

object App extends scala.App with ShutdownListener {
  import whitewerx.com.trapos.gateway.TextMessageGateway.logger
  import whitewerx.com.trapos.gateway.TextMessageGateway.formatter._

  /**This is the number of event processors + 1 thread for the gateway */
  val THREAD_POOL_SIZE: Int = 4
  val RINGBUFFER_SIZE: Int = 16

  /**
   * Thread pool for disruptor threads.
   */
  val threadPool: ExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
  val tasks: Array[JFuture[_]] = new Array[JFuture[_]](THREAD_POOL_SIZE)
  val eventProcessors: Array[EventProcessor] = new Array[EventProcessor](THREAD_POOL_SIZE - 1)
  val shutdown: CountDownLatch = new CountDownLatch(1)

  // This is to keep my MBA from catching on fire...
  val waitStrategy: WaitStrategy = new BlockingWaitStrategy
  val ringBuffer: RingBuffer[MarketEvent] = new RingBuffer[MarketEvent](MarketEvent.FACTORY, claimStrategy, waitStrategy)
  // Initial barrier
  val translationBarrier: SequenceBarrier = ringBuffer.newBarrier()
  val tradeProcessor: EventProcessor = createTradeProcessor(ringBuffer, translationBarrier)
  val rateProcessor: EventProcessor = createRateProcessor(ringBuffer, translationBarrier)
  // Add the portfolio position aggregator with a barrier after both
  // processors.
  val positionBarrier: SequenceBarrier = ringBuffer.newBarrier(tradeProcessor.getSequence, rateProcessor.getSequence)
  val portfolioPositionProcessor: EventProcessor = createPortfolioPositionProcessor(ringBuffer, positionBarrier)
  // Netty Event Publisher
  val gateway: TextMessageGateway = createGatewayEventPublisher(ringBuffer)
  // The producer can't move past this barrier.
  ringBuffer.setGatingSequences(tradeProcessor.getSequence, rateProcessor.getSequence, portfolioPositionProcessor.getSequence)

  eventProcessors(0) = tradeProcessor
  eventProcessors(1) = rateProcessor
  eventProcessors(2) = portfolioPositionProcessor

  // Start the threads
  tasks(0) = threadPool.submit(gateway)
  tasks(1) = threadPool.submit(tradeProcessor)
  tasks(2) = threadPool.submit(rateProcessor)
  tasks(3) = threadPool.submit(portfolioPositionProcessor)

  shutdown.await
  logger.info("Shutting down the app.")

  /**
   * G* in the README.md
   *
   * @param ringBuffer
   * @return
   */
  private def createGatewayEventPublisher(ringBuffer: RingBuffer[MarketEvent]): TextMessageGateway = {
    val eventPublisher: MarketEventPublisher = new MarketEventPublisher(ringBuffer.asInstanceOf[MarketEventPublisher.RingBuffer])
    new TextMessageGateway(eventPublisher, this)
  }

  /**
   * PP in the README.md
   *
   * @param ringBuffer
   * @param positionBarrier
   * @return
   */
  private def createPortfolioPositionProcessor(ringBuffer: RingBuffer[MarketEvent], positionBarrier: SequenceBarrier): EventProcessor = {
    val portfolioPositionHandler = new PortfolioPositionHandler(new PortfolioPosition(Position))
    new BatchEventProcessor[MarketEvent](ringBuffer, positionBarrier, portfolioPositionHandler)
  }

  /**
   * RT in the README.md
   *
   * @param ringBuffer
   * @param translationBarrier
   * @return
   */
  private def createRateProcessor(ringBuffer: RingBuffer[MarketEvent], translationBarrier: SequenceBarrier): EventProcessor = {
    val rateHandler: MarketRateEventHandler = new MarketRateEventHandler(RateTranslator)
    new BatchEventProcessor[MarketEvent](ringBuffer, translationBarrier, rateHandler)
  }

  /**
   * TT in the README.md
   *
   * @param ringBuffer
   * @param translationBarrier
   * @return
   */
  private def createTradeProcessor(ringBuffer: RingBuffer[MarketEvent], translationBarrier: SequenceBarrier): EventProcessor = {
    val tradeHandler: MarketTradeEventHandler = new MarketTradeEventHandler(TradeTranslator)
    new BatchEventProcessor[MarketEvent](ringBuffer, translationBarrier, tradeHandler)
  }

  /**
   * The sequence claim strategy for the producer is dependent on the number
   * of threads in the gateway.
   */
  private def claimStrategy: ClaimStrategy = {
    TextMessageGateway.PUBLISHING_THREADS match {
      case 1 => new SingleThreadedClaimStrategy(RINGBUFFER_SIZE)
      case _ => new MultiThreadedClaimStrategy(RINGBUFFER_SIZE)
    }
  }

  def notifyShutdown {
    shutdownDisruptor
    shutdownThreadPool
    shutdown.countDown
  }

  private def shutdownDisruptor {
    for (p <- eventProcessors) {
      p.halt
    }
    for (task <- tasks) {
      task.cancel(true)
    }
  }

  private def shutdownThreadPool {
    threadPool.shutdown
    try {
      threadPool.awaitTermination(10, TimeUnit.SECONDS)
    }
    catch {
      case e: InterruptedException => {
        // ignore as we are shutting down anyway.
      }
    }
  }
}
