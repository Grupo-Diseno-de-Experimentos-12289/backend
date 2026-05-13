package pe.edu.upc.travelmatch.geolocationv2.domain.model.commands;

/**
 * Update destination command.
 *
 * @param destinationId the destination id
 * @param name          the name
 * @param address       the address
 * @param district      the district
 * @param city          the city
 * @param state         the state
 * @param country       the country
 */
public record UpdateDestinationCommand(
    Long destinationId,
    String name,
    String address,
    String district,
    String city,
    String state,
    String country) {
}
