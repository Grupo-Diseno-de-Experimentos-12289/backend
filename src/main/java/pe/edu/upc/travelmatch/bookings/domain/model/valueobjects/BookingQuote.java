package pe.edu.upc.travelmatch.bookings.domain.model.valueobjects;

/**
 * Read model that summarizes price, availability and cancellation policy for a potential booking,
 * allowing the tourist to review this information before confirming a reservation.
 *
 * @param availabilityId the availability ID
 * @param ticketTypeId the ticket type ID
 * @param quantity the requested quantity
 * @param unitPrice the price per ticket
 * @param totalPrice the total price for the requested quantity
 * @param stockAvailable whether there is enough stock for the requested quantity
 * @param cancellationPolicy a human-readable description of the cancellation conditions
 */
public record BookingQuote(
        Long availabilityId,
        Long ticketTypeId,
        int quantity,
        Money unitPrice,
        Money totalPrice,
        boolean stockAvailable,
        String cancellationPolicy) {}