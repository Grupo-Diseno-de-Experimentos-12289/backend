package pe.edu.upc.travelmatch.profiles.domain.model.entities;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CartItemTest {

    @Test
    void testCartItemConstructorAndGetters() {
        // Arrange
        AvailabilityId availabilityId = new AvailabilityId(10L);
        Quantity quantity = new Quantity(2);
        Money price = new Money(new BigDecimal("50.0"), "USD");

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
        CartItem cartItem = new CartItem(new AvailabilityId(10L), new Quantity(2), new Money(new BigDecimal("50.0"), "USD"));
        Quantity newQuantity = new Quantity(5);
        Cart newCart = new Cart(new UserId(1L));

        // Act
        cartItem.setQuantity(newQuantity);
        cartItem.setCart(newCart);

        // Assert
        assertEquals(newQuantity, cartItem.getQuantity());
        assertEquals(newCart, cartItem.getCart());
    }
}
