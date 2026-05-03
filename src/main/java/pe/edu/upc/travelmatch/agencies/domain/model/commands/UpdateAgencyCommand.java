package pe.edu.upc.travelmatch.agencies.domain.model.commands;

public record UpdateAgencyCommand(
        Long agencyId,
        String name,
        String description,
        String contactEmail,
        String contactPhone
) {}