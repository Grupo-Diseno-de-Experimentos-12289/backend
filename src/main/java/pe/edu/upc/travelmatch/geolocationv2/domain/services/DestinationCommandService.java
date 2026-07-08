package pe.edu.upc.travelmatch.geolocationv2.domain.services;

import java.util.Optional;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.CreateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.DeleteDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.UpdateDestinationCommand;

/** DestinationCommandService contract. */
public interface DestinationCommandService {
  /** Handle. */
  Long handle(CreateDestinationCommand command);

  /** Handle. */
  Optional<Destination> handle(UpdateDestinationCommand command);

  /** Handle. */
  void handle(DeleteDestinationCommand command);
}
