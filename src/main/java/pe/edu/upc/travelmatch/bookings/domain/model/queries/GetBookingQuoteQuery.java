package pe.edu.upc.travelmatch.bookings.domain.model.queries;

/**
 * Query to obtain a booking quote before confirming a reservation.
 *
 * @param availabilityId the availability ID
 * @param ticketTypeId the ticket type ID
 * @param quantity the number of tickets requested
 */
public record GetBookingQuoteQuery(Long availabilityId, Long ticketTypeId, int quantity) {
    /** Documentation. */
    public GetBookingQuoteQuery {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}