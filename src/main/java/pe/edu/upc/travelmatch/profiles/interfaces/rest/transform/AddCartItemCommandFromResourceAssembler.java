package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.commands.AddCartItemCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.AddCartItemResource;

public class AddCartItemCommandFromResourceAssembler {
    public static AddCartItemCommand toCommandFromResource(Long userId, AddCartItemResource resource) {
        return new AddCartItemCommand(
                new UserId(userId),
                new AvailabilityId(resource.availabilityId()),
                new Quantity(resource.quantity())
        );
    }
}
