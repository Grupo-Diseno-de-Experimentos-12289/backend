package pe.edu.upc.travelmatch.bookings.domain.model.commands;

/** CancelBookingCommand value carrier. */
public record CancelBookingCommand(Long bookingId, Long userId, String reason) {}
