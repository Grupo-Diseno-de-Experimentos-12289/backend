package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform;

import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.CreateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.CreateDestinationResource;

/**
 * Assembler to convert CreateDestinationResource to CreateDestinationCommand.
 */
public class CreateDestinationCommandFromResourceAssembler {

  /**
   * Convert resource to command.
   *
   * @param resource the resource
   * @return the command
   */
  public static CreateDestinationCommand toCommandFromResource(
      CreateDestinationResource resource) {
    return new CreateDestinationCommand(
        resource.name(),
        resource.address(),
        resource.district(),
        resource.city(),
        resource.state(),
        resource.country()
    );
  }
}
