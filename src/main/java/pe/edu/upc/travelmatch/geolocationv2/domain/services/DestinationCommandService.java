package pe.edu.upc.travelmatch.geolocationv2.domain.services;

import java.util.Optional;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.CreateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.DeleteDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.UpdateDestinationCommand;

/**
 * Destination command service.
 */
public interface DestinationCommandService {

  /**
   * Handle create destination command.
   *
   * @param command the create command
   * @return the destination id
   */
  Long handle(CreateDestinationCommand command);

  /**
   * Handle update destination command.
   *
   * @param command the update command
   * @return the destination if successful
   */
  Optional<Destination> handle(UpdateDestinationCommand command);

  /**
   * Handle delete destination command.
   *
   * @param command the delete command
   */
  void handle(DeleteDestinationCommand command);
}
