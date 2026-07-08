package pe.edu.upc.travelmatch.geolocationv2.domain.model.commands;

/** UpdateDestinationCommand value carrier. */
public record UpdateDestinationCommand(
    Long destinationId,
    String name,
    String address,
    String district,
    String city,
    String state,
    String country) {}
