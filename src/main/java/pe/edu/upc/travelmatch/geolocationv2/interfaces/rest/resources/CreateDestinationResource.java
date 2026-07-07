package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources;

/** CreateDestinationResource value carrier. */
public record CreateDestinationResource(
    String name, String address, String district, String city, String state, String country) {}
