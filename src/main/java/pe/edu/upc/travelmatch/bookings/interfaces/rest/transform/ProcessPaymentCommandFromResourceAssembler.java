package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.commands.ProcessPaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.TransactionId;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.PaymentResource;

/** ProcessPaymentCommandFromResourceAssembler type. */
public class ProcessPaymentCommandFromResourceAssembler {
  /** To command from resource. */
  public static ProcessPaymentCommand toCommandFromResource(PaymentResource resource) {
    return new ProcessPaymentCommand(
        resource.bookingId(),
        resource.paymentMethod(),
        new TransactionId(resource.transactionId()));
  }
}
