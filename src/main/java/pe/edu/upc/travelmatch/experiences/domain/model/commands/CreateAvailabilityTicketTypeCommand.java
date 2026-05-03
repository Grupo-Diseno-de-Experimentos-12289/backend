
package pe.edu.upc.travelmatch.experiences.domain.model.commands;

import java.math.BigDecimal;

public record CreateAvailabilityTicketTypeCommand(
       Long availabilityId,
       Long ticketTypeId,
       BigDecimal price,
       int stock
) {}
