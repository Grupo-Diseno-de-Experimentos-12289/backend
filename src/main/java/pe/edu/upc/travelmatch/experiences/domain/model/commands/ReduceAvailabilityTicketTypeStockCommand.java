package pe.edu.upc.travelmatch.experiences.domain.model.commands;

public record ReduceAvailabilityTicketTypeStockCommand(
        Long availabilityId,
        Long ticketTypeId,
        int quantity
) {}
