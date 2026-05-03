package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.AvailabilityResource;

public class AvailabilityResourceFromEntityAssembler {
    public static AvailabilityResource toResourceFromEntity(Availability entity) {
        return new AvailabilityResource(
                entity.getId(),
                entity.getExperience().getId(),
                entity.getStartDateTime(),
                entity.getEndDateTime(),
                entity.getCapacity()
        );
    }
}