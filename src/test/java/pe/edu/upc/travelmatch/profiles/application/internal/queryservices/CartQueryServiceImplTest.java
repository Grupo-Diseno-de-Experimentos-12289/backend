package pe.edu.upc.travelmatch.profiles.application.internal.queryservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemByUserIdAndAvailabilityIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetCartItemCountQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.CartRepository;

@ExtendWith(MockitoExtension.class)
class CartQueryServiceImplTest {

  @Mock private CartRepository cartRepository;

  @InjectMocks private CartQueryServiceImpl cartQueryService;

  @Test
  void testHandle_GetCartByUserId() {
    // Arrange
    UserId userId = new UserId(1L);
    GetCartByUserIdQuery query = new GetCartByUserIdQuery(userId);
    Cart expectedCart = new Cart(userId);

    when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(expectedCart));

    // Act
    Optional<Cart> result = cartQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent(), "Debe retornar el carrito si existe");
    assertEquals(userId, result.get().getUserId());
    verify(cartRepository, times(1)).findByUserId(userId);
  }

  @Test
  void testHandle_GetCartItemByUserIdAndAvailabilityId() {
    // Arrange
    UserId userId = new UserId(1L);
    AvailabilityId availabilityId = new AvailabilityId(10L);
    GetCartItemByUserIdAndAvailabilityIdQuery query =
        new GetCartItemByUserIdAndAvailabilityIdQuery(userId, availabilityId);

    pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money price =
        new pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money(
            new BigDecimal("10.0"), "PEN");
    CartItem expectedCartItem = new CartItem(availabilityId, new Quantity(1), price);

    when(cartRepository.findCartItemByUserIdAndAvailabilityId(userId, availabilityId))
        .thenReturn(Optional.of(expectedCartItem));

    // Act
    Optional<CartItem> result = cartQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent(), "Debe retornar el item del carrito si existe");
    assertEquals(availabilityId, result.get().getAvailabilityId());
    verify(cartRepository, times(1)).findCartItemByUserIdAndAvailabilityId(userId, availabilityId);
  }

  @Test
  void testHandle_GetCartItemCount() {
    // Arrange
    UserId userId = new UserId(1L);
    GetCartItemCountQuery query = new GetCartItemCountQuery(userId);
    int expectedCount = 5;

    when(cartRepository.countCartItemsByUserId(userId)).thenReturn(expectedCount);

    // Act
    int result = cartQueryService.handle(query);

    // Assert
    assertEquals(expectedCount, result);
    verify(cartRepository, times(1)).countCartItemsByUserId(userId);
  }
}
