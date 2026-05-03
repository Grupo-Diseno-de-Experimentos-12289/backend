package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;


public record InitiatePaymentResource(
        Long bookingId,
        String paymentMethod
) {}