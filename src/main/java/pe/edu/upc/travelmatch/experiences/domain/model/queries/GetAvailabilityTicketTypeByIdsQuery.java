package pe.edu.upc.travelmatch.experiences.domain.model.queries;

public record GetAvailabilityTicketTypeByIdsQuery(
        Long availabilityId,
        Long ticketTypeId
) {}
