package pe.edu.upc.travelmatch.agencies.interfaces.rest.resources;

public record AgencyResource(
        Long id,
        String name,
        String description,
        String ruc,
        String contactEmail,
        String contactPhone
) {}