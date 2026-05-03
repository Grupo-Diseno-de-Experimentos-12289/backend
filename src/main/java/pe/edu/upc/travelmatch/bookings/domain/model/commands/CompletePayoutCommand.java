package pe.edu.upc.travelmatch.bookings.domain.model.commands;

import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.TransactionId;

public record CompletePayoutCommand(
        Long payoutId,
        TransactionId transactionId,
        Long agencyId
) { }
