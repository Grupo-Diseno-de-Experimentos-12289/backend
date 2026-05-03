package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform;

import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.DestinationResource;

public class DestinationResourceFromEntityAssembler {
    public static DestinationResource toResourceFromEntity(Destination entity){
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
