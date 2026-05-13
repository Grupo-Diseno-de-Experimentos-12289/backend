package pe.edu.upc.travelmatch.profiles.domain.services;

import java.util.Optional;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.AddCartItemCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.ClearCartCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateCartCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.RemoveCartItemCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.UpdateCartItemQuantityCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;

/** CartCommandService contract. */
public interface CartCommandService {
  /** Handle. */
  Long handle(CreateCartCommand command);

  /** Handle. */
  Optional<Cart> handle(ClearCartCommand command);

  /** Handle. */
  Optional<CartItem> handle(AddCartItemCommand command);

  /** Handle. */
  Optional<Cart> handle(RemoveCartItemCommand command);

  /** Handle. */
  Optional<CartItem> handle(UpdateCartItemQuantityCommand command);
}
