package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyResource;

public class AgencyResourceFromAgencyAssembler {
    public static AgencyResource toResourceFromEntity(Agency entity) {
        return new AgencyResource(
                entity.getId(),
                entity.getName().getName(),
                entity.getDescription(),
                entity.getRuc(),
                entity.getContactEmail(),
                entity.getContactPhone()
        );
    }
}