package pe.edu.upc.travelmatch.bookings.domain.model.commands;

/** FailPaymentCommand value carrier. */
public record FailPaymentCommand(Long bookingId, String reason) {}
