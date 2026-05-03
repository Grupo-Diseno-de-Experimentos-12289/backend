package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateAvailabilityResource;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
public class CreateAvailabilityCommandFromResourceAssembler {
    public static CreateAvailabilityCommand toCommandFromResource(CreateAvailabilityResource resource, Experience experience) {
        return new CreateAvailabilityCommand(
                experience,
                resource.startDateTime(),
                resource.endDateTime(),
                resource.capacity()
        );
    }
}