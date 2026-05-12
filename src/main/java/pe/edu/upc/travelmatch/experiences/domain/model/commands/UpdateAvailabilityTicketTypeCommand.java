package pe.edu.upc.travelmatch.experiences.domain.model.commands;
import java.math.BigDecimal;
/**
 * Command to update an availability ticket type.
 *
 * @param availabilityId the availability ID
 * @param ticketTypeId   the ticket type ID
 * @param newPrice       the new price
 * @param newStock       the new stock
 */
public record UpdateAvailabilityTicketTypeCommand(
    Long availabilityId,
    Long ticketTypeId,
    BigDecimal newPrice,
    int newStock) {
}
