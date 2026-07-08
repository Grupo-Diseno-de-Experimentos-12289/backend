package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;

import java.math.BigDecimal;

/** BookingQuoteResource value carrier. */
public record BookingQuoteResource(
        Long availabilityId,
        Long ticketTypeId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        String currency,
        boolean stockAvailable,
        String cancellationPolicy) {}