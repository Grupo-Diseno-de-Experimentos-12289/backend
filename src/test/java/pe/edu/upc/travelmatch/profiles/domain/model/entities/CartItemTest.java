package pe.edu.upc.travelmatch.profiles.domain.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;

@ExtendWith(MockitoExtension.class)
class CartItemTest {

  @Mock private AvailabilityId availabilityId;

  @Mock private Quantity quantity;

  @Mock private Quantity newQuantity;

  @Mock private Money price;

  @Mock private Cart newCart;

  @Test
  void testCartItemConstructorAndGetters() {
    // Act
    CartItem cartItem = new CartItem(availabilityId, quantity, price);

    // Assert
    assertEquals(availabilityId, cartItem.getAvailabilityId());
    assertEquals(quantity, cartItem.getQuantity());
    assertEquals(price, cartItem.getPrice());
    assertNull(cartItem.getId()); // Should be null before persisting
    assertNull(cartItem.getCart()); // Should be null initially
  }

  @Test
  void testCartItemSetters() {
    // Arrange
    CartItem cartItem = new CartItem(availabilityId, quantity, price);

    // Act
    cartItem.setQuantity(newQuantity);
    cartItem.setCart(newCart);

    // Assert
    assertEquals(newQuantity, cartItem.getQuantity());
    assertEquals(newCart, cartItem.getCart());
  }
}
