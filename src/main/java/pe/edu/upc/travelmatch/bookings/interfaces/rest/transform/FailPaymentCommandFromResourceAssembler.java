package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.commands.FailPaymentCommand;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.FailPaymentResource;

public class FailPaymentCommandFromResourceAssembler {
    public static FailPaymentCommand toCommandFromResource(FailPaymentResource resource) {
        return new FailPaymentCommand(resource.bookingId(), resource.failureReason());
    }
}
