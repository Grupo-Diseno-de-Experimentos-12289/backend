package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.commands.RemoveCartItemCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.RemoveCartItemResource;

public class RemoveCartItemCommandFromResourceAssembler {
    public static RemoveCartItemCommand toCommandFromResource(Long userId, RemoveCartItemResource resource) {
        return new RemoveCartItemCommand(
                new UserId(userId),
                new AvailabilityId(resource.availabilityId())
        );
    }
}
