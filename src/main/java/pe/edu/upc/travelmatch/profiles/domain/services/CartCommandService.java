package pe.edu.upc.travelmatch.profiles.domain.services;

import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.*;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;

import java.util.Optional;

public interface CartCommandService {
    Long handle(CreateCartCommand command);
    Optional<Cart> handle(ClearCartCommand command);
    Optional<CartItem> handle(AddCartItemCommand command);
    Optional<Cart> handle(RemoveCartItemCommand command);
    Optional<CartItem> handle(UpdateCartItemQuantityCommand command);
}
