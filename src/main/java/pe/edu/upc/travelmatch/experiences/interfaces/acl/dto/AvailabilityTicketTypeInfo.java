package pe.edu.upc.travelmatch.experiences.interfaces.acl.dto;

import java.math.BigDecimal;

public record AvailabilityTicketTypeInfo(
        Long ticketTypeId,
        String ticketTypeName,
        BigDecimal price,
        int stock
) {}
