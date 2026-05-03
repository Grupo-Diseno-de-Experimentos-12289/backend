package pe.edu.upc.travelmatch.bookings.domain.model.commands;

import java.time.Instant;

public record CreateBookingCommand(
        Long userId,
        Long availabilityId,
        Long ticketTypeId,
        int quantity,
        Instant bookingDate
) { }
