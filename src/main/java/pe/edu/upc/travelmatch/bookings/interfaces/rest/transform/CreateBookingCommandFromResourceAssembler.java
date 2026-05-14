package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.commands.CreateBookingCommand;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.CreateBookingResource;

/** CreateBookingCommandFromResourceAssembler type. */
public class CreateBookingCommandFromResourceAssembler {
  /** To command from resource. */
  public static CreateBookingCommand toCommandFromResource(CreateBookingResource resource) {
    return new CreateBookingCommand(
        resource.userId(),
        resource.availabilityId(),
        resource.ticketTypeId(),
        resource.quantity(),
        resource.bookingDate());
  }
}
