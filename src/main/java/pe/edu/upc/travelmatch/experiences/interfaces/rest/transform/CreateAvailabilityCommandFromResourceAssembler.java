package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateAvailabilityResource;

/** CreateAvailabilityCommandFromResourceAssembler. */
public class CreateAvailabilityCommandFromResourceAssembler {
  /** To command from resource. */
  public static CreateAvailabilityCommand toCommandFromResource(
      CreateAvailabilityResource resource, Experience experience) {
    return new CreateAvailabilityCommand(
        experience, resource.startDateTime(), resource.endDateTime(), resource.capacity());
  }
}
