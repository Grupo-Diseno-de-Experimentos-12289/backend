package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.commands.CancelBookingCommand;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.CancelBookingResource;

public class CancelBookingCommandFromResourceAssembler {
    public static CancelBookingCommand toCommandFromResource(Long bookingId, CancelBookingResource resource) {
        return new CancelBookingCommand(bookingId, resource.userId(), resource.reason());
    }
}