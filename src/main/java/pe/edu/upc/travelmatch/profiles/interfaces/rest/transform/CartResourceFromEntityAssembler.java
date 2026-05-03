package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CartItemResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CartResource;

import java.util.List;

public class CartResourceFromEntityAssembler {
    public static CartResource toResourceFromEntity(Cart entity) {
        List<CartItemResource> items = entity.getItems().stream()
                .map(CartItemResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new CartResource(entity.getId(), entity.getUserId().userId(), items);
    }
}
