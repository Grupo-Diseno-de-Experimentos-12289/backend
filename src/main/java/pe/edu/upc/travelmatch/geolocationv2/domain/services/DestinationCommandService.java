package pe.edu.upc.travelmatch.geolocationv2.domain.services;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.DeleteDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.UpdateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.CreateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;

import java.util.Optional;

public interface DestinationCommandService {
    Long handle (CreateDestinationCommand command);
    Optional<Destination> handle (UpdateDestinationCommand command);
    void handle(DeleteDestinationCommand command);
}
