package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.commands.InitiateRefundCommand;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.InitiateRefundResource;

/** InitiateRefundCommandFromResourceAssembler type. */
public class InitiateRefundCommandFromResourceAssembler {
  /** To command from resource. */
  public static InitiateRefundCommand toCommandFromResource(
      Long bookingId, InitiateRefundResource resource) {
    return new InitiateRefundCommand(bookingId, resource.refundReason());
  }
}
