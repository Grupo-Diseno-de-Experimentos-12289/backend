package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

import java.math.BigDecimal;

/** CreateAvailabilityTicketTypeResource(. */
public record CreateAvailabilityTicketTypeResource(
    Long availabilityId,
    Long ticketTypeId,
    String ticketType, // ya estÃ¡ correcto
    BigDecimal price,
    int stock) {}
