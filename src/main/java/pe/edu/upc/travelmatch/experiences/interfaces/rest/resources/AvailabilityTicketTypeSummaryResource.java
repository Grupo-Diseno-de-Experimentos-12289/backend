package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

import java.math.BigDecimal;

/**
 * Summary of a ticket type's price and remaining stock for a given availability.
 *
 * @param ticketType the ticket type name
 * @param price the price for this ticket type
 * @param stock the remaining stock for this ticket type
 */
public record AvailabilityTicketTypeSummaryResource(
    String ticketType, BigDecimal price, int stock) {}
