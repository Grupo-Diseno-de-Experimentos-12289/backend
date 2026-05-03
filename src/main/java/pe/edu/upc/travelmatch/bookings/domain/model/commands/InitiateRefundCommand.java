package pe.edu.upc.travelmatch.bookings.domain.model.commands;

public record InitiateRefundCommand(
        Long bookingId,
        String refundReason
) { }
