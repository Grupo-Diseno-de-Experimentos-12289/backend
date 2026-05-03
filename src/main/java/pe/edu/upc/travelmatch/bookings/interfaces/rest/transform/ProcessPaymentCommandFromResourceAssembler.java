package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.commands.ProcessPaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.TransactionId;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.PaymentResource;

public class ProcessPaymentCommandFromResourceAssembler {
    public static ProcessPaymentCommand toCommandFromResource(PaymentResource resource) {
        return new ProcessPaymentCommand(
                resource.bookingId(),
                resource.paymentMethod(),
                new TransactionId(resource.transactionId())
        );
    }
}
