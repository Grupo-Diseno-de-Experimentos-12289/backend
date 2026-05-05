package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartResourceFromEntityAssemblerTest {

    @Mock
    private Cart entity;

    @Mock
    private CartItem cartItem;

    @Test
    void toResourceFromEntityMapsEntityToResourceIncludingItems() {
        var price = BigDecimal.valueOf(49.99);
        when(cartItem.getAvailabilityId()).thenReturn(new AvailabilityId(101L));
        when(cartItem.getQuantity()).thenReturn(new Quantity(2));
        when(cartItem.getPrice()).thenReturn(new Money(price, "PEN"));
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
