package pe.edu.upc.travelmatch.agencies.domain.model.commands;

/** CreateAgencyCommand value carrier. */
public record CreateAgencyCommand(
    String name,
    String description,
    String ruc,
    String contactEmail,
    String contactPhone,
    Long userId) {}
