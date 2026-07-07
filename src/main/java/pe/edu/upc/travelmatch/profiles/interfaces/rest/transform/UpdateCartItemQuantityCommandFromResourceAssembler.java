package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.commands.UpdateCartItemQuantityCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.UpdateCartItemQuantityResource;

/** UpdateCartItemQuantityCommandFromResourceAssembler type. */
public class UpdateCartItemQuantityCommandFromResourceAssembler {
  /** To command from resource. */
  public static UpdateCartItemQuantityCommand toCommandFromResource(
      Long userId, UpdateCartItemQuantityResource resource) {
    return new UpdateCartItemQuantityCommand(
        new UserId(userId),
        new AvailabilityId(resource.availabilityId()),
        new Quantity(resource.newQuantity()));
  }
}
