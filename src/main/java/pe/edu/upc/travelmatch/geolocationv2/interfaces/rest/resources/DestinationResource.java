package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources;

/** DestinationResource value carrier. */
public record DestinationResource(
    Long id,
    String name,
    String address,
    String district,
    String city,
    String state,
    String country) {}
