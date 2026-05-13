package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform;

import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.DestinationResource;

/**
 * Assembler to convert Destination entity to DestinationResource.
 */
public class DestinationResourceFromEntityAssembler {

  /**
   * Convert entity to resource.
   *
   * @param entity the entity
   * @return the resource
   */
  public static DestinationResource toResourceFromEntity(Destination entity) {
    return new DestinationResource(
        entity.getId(),
        entity.getName().name(),
        entity.getAddress().address(),
        entity.getDistrict().district(),
        entity.getCity().city(),
        entity.getState().state(),
        entity.getCountry().country());
  }
}
