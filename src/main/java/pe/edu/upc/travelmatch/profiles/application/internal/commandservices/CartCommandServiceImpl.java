package pe.edu.upc.travelmatch.profiles.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.*;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.profiles.domain.services.CartCommandService;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.CartRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartCommandServiceImpl implements CartCommandService {
    private final CartRepository cartRepository;
    private final ExternalIamService externalIamService;
    private final ExternalExperienceService externalExperiencesService;

    public CartCommandServiceImpl(CartRepository cartRepository, ExternalIamService externalIamService, ExternalExperienceService externalExperiencesService) {
        this.cartRepository = cartRepository;
        this.externalIamService = externalIamService;
        this.externalExperiencesService = externalExperiencesService;
    }

    @Override
    public Long handle(CreateCartCommand command) {
        if(!externalIamService.existsUserById(command.userId()))
            throw new IllegalArgumentException("User with id " + command.userId().userId()+ " not found");
        var cart = new Cart(command.userId());
        cartRepository.save(cart);
        return cart.getId();
    }

    @Override
    public Optional<Cart> handle(ClearCartCommand command) {
        Optional<Cart> cart = cartRepository.findByUserId(command.userId());
        return cart.map(c -> {
            c.getItems().clear();
            return cartRepository.save(c);
        });
    }

    @Override
    public Optional<CartItem> handle(AddCartItemCommand command) {
        Optional<Cart> cart = cartRepository.findByUserId(command.userId());
        if (cart.isEmpty()) {
            throw new IllegalStateException("No cart found for the given user.");
        }

//        if (!externalExperiencesService.existsExperienceById(command.availabilityId().experienceId())) {
//            throw new IllegalArgumentException("The provided availability ID does not link to a valid experience.");
//        }
        // Price value should be retrieved from Experiences BC via ACL
        Money price = new Money(new BigDecimal(10),"PEN");
        CartItem newItem = new CartItem(command.availabilityId(), command.quantity(), price);
        cart.get().addItem(newItem);
        cartRepository.save(cart.get());
        return Optional.of(newItem);
    }

    @Override
    public Optional<Cart> handle(RemoveCartItemCommand command) {
        Optional<Cart> cart = cartRepository.findByUserId(command.userId());
        cart.ifPresent(c -> {
            c.removeItem(command.availabilityId());
            cartRepository.save(c);
        });
        return cart;
    }

    @Override
    public Optional<CartItem> handle(UpdateCartItemQuantityCommand command) {
        Optional<Cart> cart = cartRepository.findByUserId(command.userId());
        if (cart.isEmpty()) {
            throw new IllegalStateException("No cart found for the given user.");
        }

        Optional<CartItem> cartItem = cart.get().getItems().stream()
                .filter(item -> item.getAvailabilityId().equals(command.availabilityId()))
                .findFirst();

        cartItem.ifPresent(item -> {
            item.setQuantity(command.newQuantity());
            cartRepository.save(cart.get());
        });

        return cartItem;
    }
}
