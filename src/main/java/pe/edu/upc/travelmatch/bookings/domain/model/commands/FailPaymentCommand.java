package pe.edu.upc.travelmatch.bookings.domain.model.commands;

public record FailPaymentCommand(
        Long bookingId,
        String reason
) { }
