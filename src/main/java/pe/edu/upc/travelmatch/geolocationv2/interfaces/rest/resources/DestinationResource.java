package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources;

/**
 * Destination resource.
 *
 * @param id       the id
 * @param name     the name
 * @param address  the address
 * @param district the district
 * @param city     the city
 * @param state    the state
 * @param country  the country
 */
public record DestinationResource(
    Long id,
    String name,
    String address,
    String district,
    String city,
    String state,
    String country) {
}
