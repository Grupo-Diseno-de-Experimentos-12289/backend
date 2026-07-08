package pe.edu.upc.travelmatch.bookings.domain.model.commands;

import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.TransactionId;

/** CompletePayoutCommand value carrier. */
public record CompletePayoutCommand(Long payoutId, TransactionId transactionId, Long agencyId) {}
