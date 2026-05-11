package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartTest {

    @Mock
    private CartItem cartItem;

    @Mock
    private CartItem anotherCartItem;

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

        // Act
        cart.addItem(cartItem);

        // Assert
        assertEquals(1, cart.getItems().size());
        assertEquals(cartItem, cart.getItems().get(0));
        verify(cartItem).setCart(cart);
    }

    @Test
    void testAddItem_ThrowsExceptionWhenItemAlreadyExists() {
        // Arrange
        Cart cart = new Cart(new UserId(1L));
        AvailabilityId availabilityId = new AvailabilityId(10L);
        when(cartItem.getAvailabilityId()).thenReturn(availabilityId);
        when(anotherCartItem.getAvailabilityId()).thenReturn(availabilityId);
        cart.addItem(cartItem);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cart.addItem(anotherCartItem));
        assertTrue(exception.getMessage().contains("already in the cart"));
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void testRemoveItem_Successfully() {
        // Arrange
        Cart cart = new Cart(new UserId(1L));
        AvailabilityId availabilityId = new AvailabilityId(10L);
        when(cartItem.getAvailabilityId()).thenReturn(availabilityId);
        cart.addItem(cartItem);

        // Act
        cart.removeItem(availabilityId);

        // Assert
        assertTrue(cart.getItems().isEmpty());
        verify(cartItem).setCart(null);
    }

    @Test
    void testRemoveItem_ThrowsExceptionWhenItemNotFound() {
        // Arrange
        Cart cart = new Cart(new UserId(1L));
        AvailabilityId availabilityIdInCart = new AvailabilityId(10L);
        AvailabilityId availabilityIdToRemove = new AvailabilityId(20L);
        when(cartItem.getAvailabilityId()).thenReturn(availabilityIdInCart);
        cart.addItem(cartItem);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cart.removeItem(availabilityIdToRemove));
        assertTrue(exception.getMessage().contains("not found in cart"));
        assertEquals(1, cart.getItems().size());
    }
}
