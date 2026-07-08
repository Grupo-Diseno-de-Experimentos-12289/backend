package pe.edu.upc.travelmatch.profiles.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.AddCartItemCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.ClearCartCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateCartCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.RemoveCartItemCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.UpdateCartItemQuantityCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemCountQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.domain.services.CartCommandService;
import pe.edu.upc.travelmatch.profiles.domain.services.CartQueryService;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.AddCartItemResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CartItemResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CartResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateCartResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.RemoveCartItemResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.UpdateCartItemQuantityResource;

@ExtendWith(MockitoExtension.class)
class CartsControllerTest {

  @Mock private CartCommandService cartCommandService;

  @Mock private CartQueryService cartQueryService;

  @InjectMocks private CartsController cartsController;

  private Cart cart;
  private CartItem cartItem;

  @BeforeEach
  void setUp() {
    // Inicializar mocks del dominio para simular respuestas estables
    cart = mock(Cart.class);
    cartItem = mock(CartItem.class);
  }

  private CartItem createCartItem(int quantity) {
    return new CartItem(
        new AvailabilityId(101L),
        new Quantity(quantity),
        new Money(BigDecimal.valueOf(49.99), "PEN"));
  }

  private void stubCartResourceFields() {
    when(cart.getId()).thenReturn(1L);
    when(cart.getUserId()).thenReturn(new UserId(1L));
    when(cart.getItems()).thenReturn(List.of());
  }

  // ==========================================
  // Tests: createCart
  // ==========================================
  @Test
  void testCreateCart_Created() {
    // Arrange
    final CreateCartResource resource = new CreateCartResource(1L);

    stubCartResourceFields();

    when(cartCommandService.handle(any(CreateCartCommand.class))).thenReturn(1L);
    when(cartQueryService.handle(any(GetCartByUserIdQuery.class))).thenReturn(Optional.of(cart));

    // Act
    ResponseEntity<CartResource> response = cartsController.createCart(resource);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testCreateCart_NotFound() {
    // Arrange
    CreateCartResource resource = new CreateCartResource(1L);
    when(cartCommandService.handle(any(CreateCartCommand.class))).thenReturn(1L);
    when(cartQueryService.handle(any(GetCartByUserIdQuery.class))).thenReturn(Optional.empty());

    // Act
    ResponseEntity<CartResource> response = cartsController.createCart(resource);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  // ==========================================
  // Tests: addItem
  // ==========================================

  @Test
  void testAddItem_Created() {
    // Arrange
    var price = BigDecimal.valueOf(49.99);
    AddCartItemResource resource = new AddCartItemResource(101L, 2, price);

    cartItem = createCartItem(2);
    when(cartCommandService.handle(any(AddCartItemCommand.class)))
        .thenReturn(Optional.of(cartItem));

    // Act
    ResponseEntity<CartItemResource> response = cartsController.addItem(1L, resource);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(101L, response.getBody().availabilityId());
    assertEquals(2, response.getBody().quantity());
    assertEquals(price, response.getBody().price());
  }

  @Test
  void testAddItem_NotFound() {
    // Arrange

    AddCartItemResource resource =
        new AddCartItemResource(101L, 2, java.math.BigDecimal.valueOf(49.99));
    when(cartCommandService.handle(any(AddCartItemCommand.class))).thenReturn(Optional.empty());

    // Act
    ResponseEntity<CartItemResource> response = cartsController.addItem(1L, resource);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testAddItem_BadRequest() {
    // Arrange
    AddCartItemResource resource =
        new AddCartItemResource(101L, -1, java.math.BigDecimal.valueOf(49.99));

    // Act
    ResponseEntity<CartItemResource> response = cartsController.addItem(1L, resource);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
  }

  // ==========================================
  // Tests: updateItemQuantity
  // ==========================================

  @Test
  void testUpdateItemQuantity_Ok() {
    // Arrange
    UpdateCartItemQuantityResource resource = new UpdateCartItemQuantityResource(101L, 5);
    cartItem = createCartItem(5);
    when(cartCommandService.handle(any(UpdateCartItemQuantityCommand.class)))
        .thenReturn(Optional.of(cartItem));

    // Act
    ResponseEntity<CartItemResource> response = cartsController.updateItemQuantity(1L, resource);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(101L, response.getBody().availabilityId());
    assertEquals(5, response.getBody().quantity());
    assertEquals(BigDecimal.valueOf(49.99), response.getBody().price());
  }

  @Test
  void testUpdateItemQuantity_NotFound() {
    // Arrange
    UpdateCartItemQuantityResource resource = new UpdateCartItemQuantityResource(101L, 5);
    when(cartCommandService.handle(any(UpdateCartItemQuantityCommand.class)))
        .thenReturn(Optional.empty());

    // Act
    ResponseEntity<CartItemResource> response = cartsController.updateItemQuantity(1L, resource);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testUpdateItemQuantity_BadRequest() {
    // Arrange
    UpdateCartItemQuantityResource resource = new UpdateCartItemQuantityResource(101L, 5);
    when(cartCommandService.handle(any(UpdateCartItemQuantityCommand.class)))
        .thenThrow(new IllegalStateException("Cart is not in an active state"));

    // Act
    ResponseEntity<CartItemResource> response = cartsController.updateItemQuantity(1L, resource);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
  }

  // ==========================================
  // Tests: removeItem
  // ==========================================

  @Test
  void testRemoveItem_Ok() {
    // Arrange
    RemoveCartItemResource resource = new RemoveCartItemResource(101L);
    stubCartResourceFields();
    when(cartCommandService.handle(any(RemoveCartItemCommand.class))).thenReturn(Optional.of(cart));

    // Act
    ResponseEntity<CartResource> response = cartsController.removeItem(1L, resource);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testRemoveItem_NotFound() {
    // Arrange
    RemoveCartItemResource resource = new RemoveCartItemResource(101L);
    when(cartCommandService.handle(any(RemoveCartItemCommand.class))).thenReturn(Optional.empty());

    // Act
    ResponseEntity<CartResource> response = cartsController.removeItem(1L, resource);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testRemoveItem_BadRequest() {
    // Arrange
    RemoveCartItemResource resource = new RemoveCartItemResource(101L);
    when(cartCommandService.handle(any(RemoveCartItemCommand.class)))
        .thenThrow(new IllegalArgumentException("Item does not exist in cart"));

    // Act
    ResponseEntity<CartResource> response = cartsController.removeItem(1L, resource);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
  }

  // ==========================================
  // Tests: clearCart
  // ==========================================

  @Test
  void testClearCart_Ok() {
    // Arrange
    stubCartResourceFields();
    when(cartCommandService.handle(any(ClearCartCommand.class))).thenReturn(Optional.of(cart));

    // Act
    ResponseEntity<CartResource> response = cartsController.clearCart(1L);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testClearCart_NotFound() {
    // Arrange
    when(cartCommandService.handle(any(ClearCartCommand.class))).thenReturn(Optional.empty());

    // Act
    ResponseEntity<CartResource> response = cartsController.clearCart(1L);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testClearCart_BadRequest() {
    // Arrange
    when(cartCommandService.handle(any(ClearCartCommand.class)))
        .thenThrow(new IllegalStateException("Cannot clear already empty or inactive cart"));

    // Act
    ResponseEntity<CartResource> response = cartsController.clearCart(1L);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
  }

  // ==========================================
  // Tests: getCart
  // ==========================================

  @Test
  void testGetCart_Ok() {
    // Arrange
    stubCartResourceFields();
    when(cartQueryService.handle(any(GetCartByUserIdQuery.class))).thenReturn(Optional.of(cart));

    // Act
    ResponseEntity<CartResource> response = cartsController.getCart(1L);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testGetCart_NotFound() {
    // Arrange
    when(cartQueryService.handle(any(GetCartByUserIdQuery.class))).thenReturn(Optional.empty());

    // Act
    ResponseEntity<CartResource> response = cartsController.getCart(1L);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  // ==========================================
  // Tests: getCartItemCount
  // ==========================================

  @Test
  void testGetCartItemCount_Ok() {
    // Arrange
    when(cartQueryService.handle(any(GetCartItemCountQuery.class))).thenReturn(3);

    // Act
    ResponseEntity<Integer> response = cartsController.getCartItemCount(1L);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(3, response.getBody());
  }
}
