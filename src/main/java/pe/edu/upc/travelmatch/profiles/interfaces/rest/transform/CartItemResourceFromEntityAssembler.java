package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CartItemResource;

public class CartItemResourceFromEntityAssembler {
    public static CartItemResource toResourceFromEntity(CartItem entity) {
        return new CartItemResource(
                entity.getAvailabilityId().availabilityId(),
                entity.getQuantity().value(),
                entity.getPrice().amount()
        );
    }
}
