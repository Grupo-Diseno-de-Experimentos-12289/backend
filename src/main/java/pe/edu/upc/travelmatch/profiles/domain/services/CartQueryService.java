package pe.edu.upc.travelmatch.profiles.domain.services;

import java.util.Optional;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemByUserIdAndAvailabilityIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemCountQuery;

/** CartQueryService contract. */
public interface CartQueryService {
  /** Handle. */
  Optional<Cart> handle(GetCartByUserIdQuery query);

  /** Handle. */
  Optional<CartItem> handle(GetCartItemByUserIdAndAvailabilityIdQuery query);

  /** Handle. */
  int handle(GetCartItemCountQuery query);
}
