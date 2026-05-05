package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartResourceFromEntityAssemblerTest {

    @Test
    void toResourceFromEntityMapsEntityToResourceIncludingItems() {
        var price = BigDecimal.valueOf(49.99);
        var cartItem = new CartItem(
                new AvailabilityId(101L),
                new Quantity(2),
                new Money(price, "PEN"));
        var entity = mock(Cart.class);
        when(entity.getId()).thenReturn(10L);
        when(entity.getUserId()).thenReturn(new UserId(1L));
        when(entity.getItems()).thenReturn(List.of(cartItem));

        var resource = CartResourceFromEntityAssembler.toResourceFromEntity(entity);

        assertEquals(10L, resource.cartId());
        assertEquals(1L, resource.userId());
        assertNotNull(resource.items());
        assertEquals(1, resource.items().size());
        assertEquals(101L, resource.items().get(0).availabilityId());
        assertEquals(2, resource.items().get(0).quantity());
        assertEquals(price, resource.items().get(0).price());
    }
}
