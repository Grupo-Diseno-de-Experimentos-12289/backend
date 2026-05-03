package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.ExperienceMediaResource;

public class ExperienceMediaResourceFromEntityAssembler {
    public static ExperienceMediaResource toResourceFromEntity(ExperienceMedia entity) {
        return new ExperienceMediaResource(
                entity.getId(),
                entity.getExperience().getId(),
                entity.getMediaUrl(),
                entity.getCaption()
        );
    }
}