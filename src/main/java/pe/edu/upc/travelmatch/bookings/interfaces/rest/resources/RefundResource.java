package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

/** RefundResource value carrier. */
public record RefundResource(
    Long id,
    Long bookingId,
    BigDecimal amount,
    String currency,
    String refundStatus,
    String refundReason,
    Instant refundDate) {}
