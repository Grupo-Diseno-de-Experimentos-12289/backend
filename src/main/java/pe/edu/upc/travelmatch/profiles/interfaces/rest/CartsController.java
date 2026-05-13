package pe.edu.upc.travelmatch.profiles.interfaces.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.ClearCartCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemCountQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.domain.services.CartCommandService;
import pe.edu.upc.travelmatch.profiles.domain.services.CartQueryService;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.AddCartItemResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CartItemResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CartResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateCartResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.RemoveCartItemResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.UpdateCartItemQuantityResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.AddCartItemCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.CartItemResourceFromEntityAssembler;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.CartResourceFromEntityAssembler;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.CreateCartCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.RemoveCartItemCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.UpdateCartItemQuantityCommandFromResourceAssembler;

/** CartsController type. */
@RestController
@RequestMapping(value = "/api/v1/carts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Carts", description = "Carts Management Endpoints")
public class CartsController {
  private static final Logger LOG = LoggerFactory.getLogger(CartsController.class);
  private final CartCommandService cartCommandService;
  private final CartQueryService cartQueryService;

  /** Constructs a new CartsController. */
  public CartsController(CartCommandService cartCommandService, CartQueryService cartQueryService) {
    this.cartCommandService = cartCommandService;
    this.cartQueryService = cartQueryService;
  }

  /** Create cart. */
  @PostMapping
  public ResponseEntity<CartResource> createCart(@RequestBody CreateCartResource resource) {
    var createCartCommand = CreateCartCommandFromResourceAssembler.toCommandFromResource(resource);
    var cartId = cartCommandService.handle(createCartCommand);
    LOG.info("Cart created with id {}", cartId);
    var getCartByUserIdQuery = new GetCartByUserIdQuery(new UserId(resource.userId()));
    var cart = cartQueryService.handle(getCartByUserIdQuery);
    if (cart.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var cartResource = CartResourceFromEntityAssembler.toResourceFromEntity(cart.get());
    return new ResponseEntity<>(cartResource, HttpStatus.CREATED);
  }

  /** Add item. */
  @PostMapping("/{userId}/items")
  public ResponseEntity<CartItemResource> addItem(
      @PathVariable Long userId, @RequestBody AddCartItemResource resource) {
    try {
      var addCartItemCommand =
          AddCartItemCommandFromResourceAssembler.toCommandFromResource(userId, resource);
      var addedCartItem = cartCommandService.handle(addCartItemCommand);
      if (addedCartItem.isEmpty()) {
        return ResponseEntity.notFound().build();
      }
      var cartItemResource =
          CartItemResourceFromEntityAssembler.toResourceFromEntity(addedCartItem.get());
      return new ResponseEntity<>(cartItemResource, HttpStatus.CREATED);
    } catch (IllegalStateException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  /** Update item quantity. */
  @PutMapping("/{userId}/items")
  public ResponseEntity<CartItemResource> updateItemQuantity(
      @PathVariable Long userId, @RequestBody UpdateCartItemQuantityResource resource) {
    try {
      var updateCartItemQuantityCommand =
          UpdateCartItemQuantityCommandFromResourceAssembler.toCommandFromResource(
              userId, resource);
      var updatedCartItem = cartCommandService.handle(updateCartItemQuantityCommand);
      if (updatedCartItem.isEmpty()) {
        return ResponseEntity.notFound().build();
      }
      var cartItemResource =
          CartItemResourceFromEntityAssembler.toResourceFromEntity(updatedCartItem.get());
      return ResponseEntity.ok(cartItemResource);
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  /** Remove item. */
  @DeleteMapping("/{userId}/items")
  public ResponseEntity<CartResource> removeItem(
      @PathVariable Long userId, @RequestBody RemoveCartItemResource resource) {
    try {
      var removeCartItemCommand =
          RemoveCartItemCommandFromResourceAssembler.toCommandFromResource(userId, resource);
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

  /** Clear cart. */
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

  /** Get cart. */
  @GetMapping("/{userId}")
  public ResponseEntity<CartResource> getCart(@PathVariable Long userId) {
    UserId userIdValueObject = new UserId(userId);
    var getCartByUserIdQuery = new GetCartByUserIdQuery(userIdValueObject);
    var cart = cartQueryService.handle(getCartByUserIdQuery);
    if (cart.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var cartResource = CartResourceFromEntityAssembler.toResourceFromEntity(cart.get());
    return new ResponseEntity<>(cartResource, HttpStatus.OK);
  }

  /** Get cart item count. */
  @GetMapping("/{userId}/items/count")
  public ResponseEntity<Integer> getCartItemCount(@PathVariable Long userId) {
    UserId userIdValueObject = new UserId(userId);
    var getCartItemCountQuery = new GetCartItemCountQuery(userIdValueObject);
    var cartItemCount = cartQueryService.handle(getCartItemCountQuery);
    return new ResponseEntity<>(cartItemCount, HttpStatus.OK);
  }
}
