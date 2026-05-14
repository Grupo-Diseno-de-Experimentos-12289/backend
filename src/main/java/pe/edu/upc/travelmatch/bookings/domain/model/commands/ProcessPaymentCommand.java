package pe.edu.upc.travelmatch.bookings.domain.model.commands;

import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.TransactionId;

/** ProcessPaymentCommand value carrier. */
public record ProcessPaymentCommand(
    Long bookingId, String paymentMethod, TransactionId transactionId) {}
