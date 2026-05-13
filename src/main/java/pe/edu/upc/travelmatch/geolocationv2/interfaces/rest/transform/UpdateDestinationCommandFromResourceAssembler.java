package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform;

import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.UpdateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.DestinationResource;

/**
 * Assembler to convert DestinationResource to UpdateDestinationCommand.
 */
public class UpdateDestinationCommandFromResourceAssembler {

  /**
   * Convert resource to command.
   *
   * @param destinationId the destination id
   * @param resource      the resource
   * @return the command
   */
  public static UpdateDestinationCommand toCommandFromResource(
      Long destinationId, DestinationResource resource) {
    return new UpdateDestinationCommand(
        destinationId,
        resource.name(),
        resource.address(),
        resource.district(),
        resource.city(),
        resource.state(),
        resource.country());
  }
}
