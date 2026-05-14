package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;

/** PaymentResource value carrier. */
public record PaymentResource(Long bookingId, String paymentMethod, String transactionId) {}
