package whitewerx.com.trapos.model.event

import collection.mutable

/**
 * A way to handle notifications for events within the
 * domain.  This only allows for one handler for each
 * domain event.
 *
 * Care should be taken about the transaction boundary
 * of {@link scala.mutable.Subscriber}s.
 *
 * http://www.udidahan.com/2009/06/14/domain-events-salvation/
 *
 * @author ewhite
 */

class DomainEventsPublisher extends mutable.Publisher[Event] {
  type Pub = DomainEventsPublisher
  override def publish(event: Event) {
    super.publish(event)
  }
}

object DomainEvents extends DomainEventsPublisher {

  def raise(event: Event) {
    publish(event)
  }

}
