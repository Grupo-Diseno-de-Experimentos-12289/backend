package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources;

/**
 * Create destination resource.
 *
 * @param name     the name
 * @param address  the address
 * @param district the district
 * @param city     the city
 * @param state    the state
 * @param country  the country
 */
public record CreateDestinationResource(
    String name,
    String address,
    String district,
    String city,
    String state,
    String country) {
}
