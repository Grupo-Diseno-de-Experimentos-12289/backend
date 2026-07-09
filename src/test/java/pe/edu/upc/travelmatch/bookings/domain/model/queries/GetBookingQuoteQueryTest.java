package pe.edu.upc.travelmatch.bookings.domain.model.queries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetBookingQuoteQueryTest {

  @Test
  @DisplayName("constructor stores all fields correctly when quantity is valid")
  void constructor_validQuantity_storesFields() {
    // Arrange & Act
    var query = new GetBookingQuoteQuery(1L, 2L, 5);

    // Assert
    assertEquals(1L, query.availabilityId());
    assertEquals(2L, query.ticketTypeId());
    assertEquals(5, query.quantity());
  }

  @Test
  @DisplayName("constructor throws IllegalArgumentException when quantity is zero")
  void constructor_zeroQuantity_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> new GetBookingQuoteQuery(1L, 2L, 0));
  }

  @Test
  @DisplayName("constructor throws IllegalArgumentException when quantity is negative")
  void constructor_negativeQuantity_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> new GetBookingQuoteQuery(1L, 2L, -1));
  }
}