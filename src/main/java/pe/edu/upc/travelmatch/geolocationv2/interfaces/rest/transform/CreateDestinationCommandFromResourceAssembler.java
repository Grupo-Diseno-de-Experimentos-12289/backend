package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform;

import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.CreateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.CreateDestinationResource;

/** CreateDestinationCommandFromResourceAssembler type. */
public class CreateDestinationCommandFromResourceAssembler {
  /** To command from resource. */
  public static CreateDestinationCommand toCommandFromResource(CreateDestinationResource resource) {
    return new CreateDestinationCommand(
        resource.name(),
        resource.address(),
        resource.district(),
        resource.city(),
        resource.state(),
        resource.country());
  }
}
