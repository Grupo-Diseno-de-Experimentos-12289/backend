package pe.edu.upc.travelmatch.experiences.domain.model.commands;
import java.math.BigDecimal;
/**
 * Command to create an availability ticket type.
 *
 * @param availabilityId the availability ID
 * @param ticketTypeId   the ticket type ID
 * @param price          the price
 * @param stock          the stock
 */
public record CreateAvailabilityTicketTypeCommand(
    Long availabilityId,
    Long ticketTypeId,
    BigDecimal price,
    int stock) {
}
