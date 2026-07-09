package pe.edu.upc.travelmatch.bookings.application.internal.queryservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.bookings.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingQuoteQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingQuote;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingQuoteServiceImpl Tests")
class BookingQuoteServiceImplTest {

  @Mock
  private ExternalExperienceService externalExperienceService;

  @InjectMocks
  private BookingQuoteServiceImpl bookingQuoteService;

  private GetBookingQuoteQuery query;

  @BeforeEach
  void arrange_sharedQuery() {
    query = new GetBookingQuoteQuery(10L, 1L, 3);
  }

  @Test
  @DisplayName("handle() returns a quote with correct total price when stock is sufficient")
  void handle_validQuery_returnsQuoteWithCorrectTotal() {
    // Arrange
    when(externalExperienceService.existsAvailabilityById(10L)).thenReturn(true);
    when(externalExperienceService.hasSufficientStock(10L, 1L, 3)).thenReturn(true);
    when(externalExperienceService.getPriceForTicketType(10L, 1L))
        .thenReturn(new BigDecimal("50.00"));

    // Act
    BookingQuote quote = bookingQuoteService.handle(query);

    // Assert
    assertEquals(10L, quote.availabilityId());
    assertEquals(1L, quote.ticketTypeId());
    assertEquals(3, quote.quantity());
    assertEquals(new BigDecimal("50.00"), quote.unitPrice().getAmount());
    assertEquals(new BigDecimal("150.00"), quote.totalPrice().getAmount());
    assertTrue(quote.stockAvailable());
    assertFalse(quote.cancellationPolicy().isBlank());
  }

  @Test
  @DisplayName("handle() marks stockAvailable as false when there is not enough stock")
  void handle_insufficientStock_returnsQuoteWithStockAvailableFalse() {
    // Arrange
    when(externalExperienceService.existsAvailabilityById(10L)).thenReturn(true);
    when(externalExperienceService.hasSufficientStock(10L, 1L, 3)).thenReturn(false);
    when(externalExperienceService.getPriceForTicketType(10L, 1L))
        .thenReturn(new BigDecimal("20.00"));

    // Act
    BookingQuote quote = bookingQuoteService.handle(query);

    // Assert
    assertFalse(quote.stockAvailable());
  }

  @Test
  @DisplayName("handle() throws IllegalArgumentException when availability does not exist")
  void handle_availabilityNotFound_throwsIllegalArgument() {
    // Arrange
    when(externalExperienceService.existsAvailabilityById(10L)).thenReturn(false);

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> bookingQuoteService.handle(query));

    assertTrue(exception.getMessage().contains("does not exist"));
  }

  @Test
  @DisplayName("handle() computes single-unit total price correctly")
  void handle_singleQuantity_totalEqualsUnitPrice() {
    // Arrange
    GetBookingQuoteQuery singleQuery = new GetBookingQuoteQuery(20L, 2L, 1);
    when(externalExperienceService.existsAvailabilityById(20L)).thenReturn(true);
    when(externalExperienceService.hasSufficientStock(20L, 2L, 1)).thenReturn(true);
    when(externalExperienceService.getPriceForTicketType(20L, 2L))
        .thenReturn(new BigDecimal("99.90"));

    // Act
    BookingQuote quote = bookingQuoteService.handle(singleQuery);

    // Assert
    assertEquals(quote.unitPrice().getAmount(), quote.totalPrice().getAmount());
  }
}