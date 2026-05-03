package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateExperienceMediaResource;

public class CreateExperienceMediaCommandFromResourceAssembler {
    public static CreateExperienceMediaCommand toCommandFromResource(
            CreateExperienceMediaResource resource, Experience experience) {
        return new CreateExperienceMediaCommand(
                experience,
                resource.mediaUrl(),
                resource.caption()
        );
    }
}