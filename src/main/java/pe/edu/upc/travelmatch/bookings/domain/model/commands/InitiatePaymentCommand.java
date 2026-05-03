package pe.edu.upc.travelmatch.bookings.domain.model.commands;

public record InitiatePaymentCommand(
        Long bookingId,
        String paymentMethod
) {}
