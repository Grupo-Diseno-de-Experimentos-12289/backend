package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingQuote;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.BookingQuoteResource;

class BookingQuoteResourceFromEntityAssemblerTest {

  @Test
  @DisplayName("toResourceFromEntity should map BookingQuote to BookingQuoteResource correctly")
  void toResourceFromEntity_ShouldMapCorrectly() {
    // Arrange
    var unitPrice = new Money(new BigDecimal("25.00"), "PEN");
    var totalPrice = new Money(new BigDecimal("75.00"), "PEN");
    var quote = new BookingQuote(5L, 2L, 3, unitPrice, totalPrice, true, "Free cancellation");

    // Act
    BookingQuoteResource resource =
        BookingQuoteResourceFromEntityAssembler.toResourceFromEntity(quote);

    // Assert
    assertNotNull(resource);
    assertEquals(5L, resource.availabilityId());
    assertEquals(2L, resource.ticketTypeId());
    assertEquals(3, resource.quantity());
    assertEquals(new BigDecimal("25.00"), resource.unitPrice());
    assertEquals(new BigDecimal("75.00"), resource.totalPrice());
    assertEquals("PEN", resource.currency());
    assertTrue(resource.stockAvailable());
    assertEquals("Free cancellation", resource.cancellationPolicy());
  }
}