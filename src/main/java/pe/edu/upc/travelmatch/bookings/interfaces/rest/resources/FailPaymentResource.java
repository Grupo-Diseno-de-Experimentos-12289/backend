package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;

/** FailPaymentResource value carrier. */
public record FailPaymentResource(Long bookingId, String failureReason) {}
