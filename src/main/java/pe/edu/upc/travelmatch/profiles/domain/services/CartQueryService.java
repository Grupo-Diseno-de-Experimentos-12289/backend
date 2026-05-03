package pe.edu.upc.travelmatch.profiles.domain.services;

import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemByUserIdAndAvailabilityIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemCountQuery;

import java.util.Optional;

public interface CartQueryService {
    Optional<Cart> handle(GetCartByUserIdQuery query);
    Optional<CartItem> handle(GetCartItemByUserIdAndAvailabilityIdQuery query);
    int handle(GetCartItemCountQuery query);
}
