package pe.edu.upc.travelmatch.experiences.domain.model.commands;

import java.math.BigDecimal;

public record UpdateAvailabilityTicketTypeCommand(
        Long availabilityId,
        Long ticketTypeId,
        BigDecimal newPrice,
        int newStock
) {}
