package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Summary of an availability slot, including remaining seats per ticket type, meant to be shown
 * to a tourist before booking.
 *
 * @param availabilityId the availability ID
 * @param startDateTime the start date and time
 * @param endDateTime the end date and time
 * @param capacity the total capacity of the slot
 * @param totalStock the sum of remaining stock across all ticket types
 * @param ticketTypes the breakdown of price/stock per ticket type
 */
public record AvailabilitySummaryResource(
    Long availabilityId,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    int capacity,
    int totalStock,
    List<AvailabilityTicketTypeSummaryResource> ticketTypes) {}
