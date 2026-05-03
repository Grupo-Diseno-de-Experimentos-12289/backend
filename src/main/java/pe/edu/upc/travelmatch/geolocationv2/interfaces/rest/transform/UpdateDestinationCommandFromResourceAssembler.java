package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform;

import pe.edu.upc.travelmatch.geolocationv2.domain.model.commands.UpdateDestinationCommand;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.DestinationResource;

public class UpdateDestinationCommandFromResourceAssembler {
    public static UpdateDestinationCommand toCommandFromResource(
     Long destinationId, DestinationResource resource){
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
