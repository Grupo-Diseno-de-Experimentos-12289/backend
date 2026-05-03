package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.ExperienceResource;

public class ExperienceResourceFromEntityAssembler {
    public static ExperienceResource toResourceFromEntity(Experience entity) {
        return new ExperienceResource(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getAgencyId().value(),
                entity.getCategory(),
                entity.getDestinationId().value(),
                entity.getDuration(),
                entity.getMeetingPoint()
        );
    }
}