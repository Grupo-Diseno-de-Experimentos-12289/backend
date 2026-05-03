package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;

public record PaymentResource(
        Long bookingId,
        String paymentMethod,
        String transactionId
) { }
