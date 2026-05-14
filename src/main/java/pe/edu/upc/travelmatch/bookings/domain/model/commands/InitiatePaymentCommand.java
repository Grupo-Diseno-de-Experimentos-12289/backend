package pe.edu.upc.travelmatch.bookings.domain.model.commands;

/** InitiatePaymentCommand value carrier. */
public record InitiatePaymentCommand(Long bookingId, String paymentMethod) {}
