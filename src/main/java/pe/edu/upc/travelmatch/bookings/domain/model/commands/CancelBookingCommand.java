package pe.edu.upc.travelmatch.bookings.domain.model.commands;

public record CancelBookingCommand(
        Long bookingId,
        Long userId,
        String reason
) { }
