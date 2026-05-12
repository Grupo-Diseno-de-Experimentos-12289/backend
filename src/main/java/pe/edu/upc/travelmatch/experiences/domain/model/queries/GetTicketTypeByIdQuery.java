package pe.edu.upc.travelmatch.experiences.domain.model.queries;
/**
 * Query to get a ticket type by its ID.
 *
 * @param ticketTypeId the ticket type ID
 */
public record GetTicketTypeByIdQuery(Long ticketTypeId) {
}
