package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;

/** InitiatePaymentResource value carrier. */
public record InitiatePaymentResource(Long bookingId, String paymentMethod) {}
