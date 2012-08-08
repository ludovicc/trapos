package whitewerx.com.trapos.model.event

/**
 * A way to handle notifications for events within the
 * domain.  This only allows for one handler for each
 * domain event.
 *
 * Care should be taken about the transaction boundary
 * of {@link EventHandler}s.
 *
 * http://www.udidahan.com/2009/06/14/domain-events-salvation/
 *
 * @author ewhite
 */
object DomainEvents {

  def raise(event: Event) {
    // TODO
  }

}
