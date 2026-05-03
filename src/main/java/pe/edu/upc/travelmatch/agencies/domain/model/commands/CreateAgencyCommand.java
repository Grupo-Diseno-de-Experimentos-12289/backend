package pe.edu.upc.travelmatch.agencies.domain.model.commands;

public record CreateAgencyCommand(
        String name,
        String description,
        String ruc,
        String contactEmail,
        String contactPhone,
        Long userId
) {}