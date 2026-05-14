package pe.edu.upc.travelmatch.bookings.domain.model.commands;

/** InitiateRefundCommand value carrier. */
public record InitiateRefundCommand(Long bookingId, String refundReason) {}
