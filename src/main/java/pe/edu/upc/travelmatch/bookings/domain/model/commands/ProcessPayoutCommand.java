package pe.edu.upc.travelmatch.bookings.domain.model.commands;

import java.math.BigDecimal;

public record ProcessPayoutCommand(
        Long bookingId,
        BigDecimal payoutFee
) { }
