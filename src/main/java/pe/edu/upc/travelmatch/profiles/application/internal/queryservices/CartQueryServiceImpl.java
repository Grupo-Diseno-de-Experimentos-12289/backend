package pe.edu.upc.travelmatch.profiles.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemByUserIdAndAvailabilityIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemCountQuery;
import pe.edu.upc.travelmatch.profiles.domain.services.CartQueryService;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.CartRepository;

import java.util.Optional;

@Service
public class CartQueryServiceImpl implements CartQueryService {

    private final CartRepository cartRepository;

    public CartQueryServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Optional<Cart> handle(GetCartByUserIdQuery query) {
        return this.cartRepository.findByUserId(query.userId());
    }

    @Override
    public Optional<CartItem> handle(GetCartItemByUserIdAndAvailabilityIdQuery query) {
        return this.cartRepository.findCartItemByUserIdAndAvailabilityId(query.userId(), query.availabilityId());
    }

    @Override
    public int handle(GetCartItemCountQuery query) {
        return this.cartRepository.countCartItemsByUserId(query.userId());
    }
}
