package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.UpdateExperienceMediaResource;

public class UpdateExperienceMediaCommandFromResourceAssembler {
    public static UpdateExperienceMediaCommand toCommandFromResource(Long id, UpdateExperienceMediaResource resource) {
        return new UpdateExperienceMediaCommand(
                id,
                resource.mediaUrl(),
                resource.caption()
        );
    }
}