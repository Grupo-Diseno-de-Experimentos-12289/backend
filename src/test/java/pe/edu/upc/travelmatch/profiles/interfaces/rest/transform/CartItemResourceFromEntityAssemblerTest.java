package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;

@ExtendWith(MockitoExtension.class)
class CartItemResourceFromEntityAssemblerTest {

  @Mock private CartItem entity;

  @Test
  void toResourceFromEntityMapsEntityToResource() {
    var price = BigDecimal.valueOf(49.99);
    when(entity.getAvailabilityId()).thenReturn(new AvailabilityId(101L));
    when(entity.getQuantity()).thenReturn(new Quantity(2));
    when(entity.getPrice()).thenReturn(new Money(price, "PEN"));

    var resource = CartItemResourceFromEntityAssembler.toResourceFromEntity(entity);

    assertEquals(101L, resource.availabilityId());
    assertEquals(2, resource.quantity());
    assertEquals(price, resource.price());
  }
}
