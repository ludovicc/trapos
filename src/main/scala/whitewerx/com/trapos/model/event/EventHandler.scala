package whitewerx.com.trapos.model.event

/**
 * @author ludo
 */

trait EventHandler[T <: Event] {

  def handle(event: T): Unit
}
