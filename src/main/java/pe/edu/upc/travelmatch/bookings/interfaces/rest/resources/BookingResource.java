package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

/** BookingResource value carrier. */
public record BookingResource(
    Long id,
    Long userId,
    Long availabilityId,
    int quantity,
    String currency,
    BigDecimal totalAmount,
    String bookingStatus,
    Instant bookingDate) {}
