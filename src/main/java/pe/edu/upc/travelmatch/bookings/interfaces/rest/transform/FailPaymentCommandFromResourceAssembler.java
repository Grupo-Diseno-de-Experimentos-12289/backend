package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.commands.FailPaymentCommand;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.FailPaymentResource;

/** FailPaymentCommandFromResourceAssembler type. */
public class FailPaymentCommandFromResourceAssembler {
  /** To command from resource. */
  public static FailPaymentCommand toCommandFromResource(FailPaymentResource resource) {
    return new FailPaymentCommand(resource.bookingId(), resource.failureReason());
  }
}
