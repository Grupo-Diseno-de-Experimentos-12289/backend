package pe.edu.upc.travelmatch.experiences.domain.model.queries;

/**
 * Query to get an availability ticket type by its IDs.
 *
 * @param availabilityId the availability ID
 * @param ticketTypeId the ticket type ID
 */
public record GetAvailabilityTicketTypeByIdsQuery(Long availabilityId, Long ticketTypeId) {}
