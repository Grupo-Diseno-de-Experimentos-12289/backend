package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;

public record FailPaymentResource(Long bookingId, String failureReason) { }
