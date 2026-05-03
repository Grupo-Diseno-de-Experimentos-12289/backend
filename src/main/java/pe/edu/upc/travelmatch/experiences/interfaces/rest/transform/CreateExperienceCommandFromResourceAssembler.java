package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateExperienceResource;

public class CreateExperienceCommandFromResourceAssembler {
    public static CreateExperienceCommand toCommandFromResource(
            CreateExperienceResource resource,
            Long agencyId
            ) {
        return new CreateExperienceCommand(
                resource.title(),
                resource.description(),
                agencyId,
                resource.category(),
                new DestinationId(resource.destinationId()),
                resource.duration(),
                resource.meetingPoint()
        );
    }
}