package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.commands.InitiatePaymentCommand;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.InitiatePaymentResource;

/** InitiatePaymentCommandFromResourceAssembler type. */
public class InitiatePaymentCommandFromResourceAssembler {

  /** To command from resource. */
  public static InitiatePaymentCommand toCommandFromResource(InitiatePaymentResource resource) {
    return new InitiatePaymentCommand(resource.bookingId(), resource.paymentMethod());
  }
}
