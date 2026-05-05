package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartItemResourceFromEntityAssemblerTest {

    @Test
    void toResourceFromEntityMapsEntityToResource() {
        var price = BigDecimal.valueOf(49.99);
        var entity = new CartItem(
                new AvailabilityId(101L),
                new Quantity(2),
                new Money(price, "PEN"));

        var resource = CartItemResourceFromEntityAssembler.toResourceFromEntity(entity);

        assertEquals(101L, resource.availabilityId());
        assertEquals(2, resource.quantity());
        assertEquals(price, resource.price());
    }
}
