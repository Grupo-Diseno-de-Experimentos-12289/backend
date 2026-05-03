package pe.edu.upc.travelmatch.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.ClearCartCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemCountQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.domain.services.CartCommandService;
import pe.edu.upc.travelmatch.profiles.domain.services.CartQueryService;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.*;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/carts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Carts", description = "Carts Management Endpoints")
public class CartsController {
    private final CartCommandService cartCommandService;
    private final CartQueryService cartQueryService;

    public CartsController(CartCommandService cartCommandService, CartQueryService cartQueryService) {
        this.cartCommandService = cartCommandService;
        this.cartQueryService = cartQueryService;
    }

    @PostMapping
    public ResponseEntity<CartResource> createCart(@RequestBody CreateCartResource resource) {
        var createCartCommand = CreateCartCommandFromResourceAssembler.toCommandFromResource(resource);
        var cartId = cartCommandService.handle(createCartCommand);
        System.out.println("Cart created with id " + cartId);
        var getCartByUserIdQuery = new GetCartByUserIdQuery(new UserId(resource.userId()));
        var cart = cartQueryService.handle(getCartByUserIdQuery);
        if(cart.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var cartResource = CartResourceFromEntityAssembler.toResourceFromEntity(cart.get());
        return new ResponseEntity<>(cartResource, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartItemResource> addItem(
            @PathVariable Long userId, @RequestBody AddCartItemResource resource) {
        try {
            var addCartItemCommand = AddCartItemCommandFromResourceAssembler.toCommandFromResource(userId, resource);
            var addedCartItem = cartCommandService.handle(addCartItemCommand);
            if (addedCartItem.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var cartItemResource = CartItemResourceFromEntityAssembler.toResourceFromEntity(addedCartItem.get());
            return new ResponseEntity<>(cartItemResource, HttpStatus.CREATED);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{userId}/items")
    public ResponseEntity<CartItemResource> updateItemQuantity(
            @PathVariable Long userId, @RequestBody UpdateCartItemQuantityResource resource) {
        try {
            var updateCartItemQuantityCommand = UpdateCartItemQuantityCommandFromResourceAssembler.toCommandFromResource(userId, resource);
            var updatedCartItem = cartCommandService.handle(updateCartItemQuantityCommand);
            if (updatedCartItem.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var cartItemResource = CartItemResourceFromEntityAssembler.toResourceFromEntity(updatedCartItem.get());
            return ResponseEntity.ok(cartItemResource);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{userId}/items")
    public ResponseEntity<CartResource> removeItem(
            @PathVariable Long userId, @RequestBody RemoveCartItemResource resource) {
        try {
            var removeCartItemCommand = RemoveCartItemCommandFromResourceAssembler.toCommandFromResource(userId, resource);
            var updatedCart = cartCommandService.handle(removeCartItemCommand);
            if (updatedCart.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var cartResource = CartResourceFromEntityAssembler.toResourceFromEntity(updatedCart.get());
            return ResponseEntity.ok(cartResource);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<CartResource> clearCart(@PathVariable Long userId) {
        try {
            var clearCartCommand = new ClearCartCommand(new UserId(userId));
            var clearedCart = cartCommandService.handle(clearCartCommand);
            if (clearedCart.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var cartResource = CartResourceFromEntityAssembler.toResourceFromEntity(clearedCart.get());
            return ResponseEntity.ok(cartResource);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResource> getCart(@PathVariable Long userId) {
        UserId userIdValueObject = new UserId(userId);
        var getCartByUserIdQuery = new GetCartByUserIdQuery(userIdValueObject);
        var cart = cartQueryService.handle(getCartByUserIdQuery);
        if(cart.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var cartResource = CartResourceFromEntityAssembler.toResourceFromEntity(cart.get());
        return new ResponseEntity<>(cartResource, HttpStatus.OK);
    }

    @GetMapping("/{userId}/items/count")
    public ResponseEntity<Integer> getCartItemCount(@PathVariable Long userId) {
        UserId userIdValueObject = new UserId(userId);
        var getCartItemCountQuery = new GetCartItemCountQuery(userIdValueObject);
        var cartItemCount = cartQueryService.handle(getCartItemCountQuery);
        return new ResponseEntity<>(cartItemCount, HttpStatus.OK);
    }
}
