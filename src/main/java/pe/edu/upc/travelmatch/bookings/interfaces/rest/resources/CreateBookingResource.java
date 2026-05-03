package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;

import java.time.Instant;

public record CreateBookingResource(
        Long userId,
        Long availabilityId,
        Long ticketTypeId,
        int quantity,
        Instant bookingDate
) { }
