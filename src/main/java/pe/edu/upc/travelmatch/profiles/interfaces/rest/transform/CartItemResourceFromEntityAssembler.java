package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CartItemResource;

/** CartItemResourceFromEntityAssembler type. */
public class CartItemResourceFromEntityAssembler {
  /** To resource from entity. */
  public static CartItemResource toResourceFromEntity(CartItem entity) {
    return new CartItemResource(
        entity.getAvailabilityId().availabilityId(),
        entity.getQuantity().value(),
        entity.getPrice().amount());
  }
}
