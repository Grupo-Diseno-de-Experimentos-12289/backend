package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

import java.math.BigDecimal;

public record CreateAvailabilityTicketTypeResource(
        Long availabilityId,
        Long ticketTypeId,
        String ticketType, // ya está correcto
        BigDecimal price,
        int stock
) {}