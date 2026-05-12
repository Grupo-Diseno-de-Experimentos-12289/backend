package pe.edu.upc.travelmatch.experiences.domain.model.commands;
/**
 * Command to reduce availability ticket type stock.
 *
 * @param availabilityId the availability ID
 * @param ticketTypeId   the ticket type ID
 * @param quantity       the quantity to reduce
 */
public record ReduceAvailabilityTicketTypeStockCommand(
    Long availabilityId,
    Long ticketTypeId,
    int quantity) {
}
