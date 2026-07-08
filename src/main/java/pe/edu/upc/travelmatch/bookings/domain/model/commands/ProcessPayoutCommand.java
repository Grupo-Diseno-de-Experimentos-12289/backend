package pe.edu.upc.travelmatch.bookings.domain.model.commands;

import java.math.BigDecimal;

/** ProcessPayoutCommand value carrier. */
public record ProcessPayoutCommand(Long bookingId, BigDecimal payoutFee) {}
