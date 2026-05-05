package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void testCartConstructorAndGetters() {
        // Arrange
        UserId userId = new UserId(1L);

        // Act
        Cart cart = new Cart(userId);

        // Assert
        assertEquals(userId, cart.getUserId());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testAddItem_Successfully() {
        // Arrange
        Cart cart = new Cart(new UserId(1L));
        AvailabilityId availabilityId = new AvailabilityId(10L);
        CartItem cartItem = new CartItem(availabilityId, new Quantity(2), new Money(new BigDecimal("50.0"), "USD"));

        // Act
        cart.addItem(cartItem);

        // Assert
        assertEquals(1, cart.getItems().size());
        assertEquals(cartItem, cart.getItems().get(0));
        assertEquals(cart, cartItem.getCart()); // verify relation is set
    }

    @Test
    void testAddItem_ThrowsExceptionWhenItemAlreadyExists() {
        // Arrange
        Cart cart = new Cart(new UserId(1L));
        AvailabilityId availabilityId = new AvailabilityId(10L);
        CartItem cartItem1 = new CartItem(availabilityId, new Quantity(2), new Money(new BigDecimal("50.0"), "USD"));
        CartItem cartItem2 = new CartItem(availabilityId, new Quantity(3), new Money(new BigDecimal("60.0"), "USD"));
        cart.addItem(cartItem1);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cart.addItem(cartItem2));
        assertTrue(exception.getMessage().contains("already in the cart"));
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void testRemoveItem_Successfully() {
        // Arrange
        Cart cart = new Cart(new UserId(1L));
        AvailabilityId availabilityId = new AvailabilityId(10L);
        CartItem cartItem = new CartItem(availabilityId, new Quantity(2), new Money(new BigDecimal("50.0"), "USD"));
        cart.addItem(cartItem);

        // Act
        cart.removeItem(availabilityId);

        // Assert
        assertTrue(cart.getItems().isEmpty());
        assertNull(cartItem.getCart()); // relation should be nullified
    }

    @Test
    void testRemoveItem_ThrowsExceptionWhenItemNotFound() {
        // Arrange
        Cart cart = new Cart(new UserId(1L));
        AvailabilityId availabilityIdInCart = new AvailabilityId(10L);
        AvailabilityId availabilityIdToRemove = new AvailabilityId(20L);
        CartItem cartItem = new CartItem(availabilityIdInCart, new Quantity(2), new Money(new BigDecimal("50.0"), "USD"));
        cart.addItem(cartItem);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cart.removeItem(availabilityIdToRemove));
        assertTrue(exception.getMessage().contains("not found in cart"));
        assertEquals(1, cart.getItems().size());
    }
}
