package pe.edu.upc.travelmatch.bookings.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingQuoteQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingQuote;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingQuoteService;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.BookingQuoteResource;

@ExtendWith(MockitoExtension.class)
class BookingQuotesControllerTest {

  @Mock
  private BookingQuoteService bookingQuoteService;

  @InjectMocks
  private BookingQuotesController controller;

  @Test
  void getBookingQuote_returnsOk_whenSuccess() {
    // Arrange
    var unitPrice = new Money(new BigDecimal("40.00"), "PEN");
    var totalPrice = new Money(new BigDecimal("80.00"), "PEN");
    var quote = new BookingQuote(10L, 1L, 2, unitPrice, totalPrice, true, "Policy text");
    when(bookingQuoteService.handle(any(GetBookingQuoteQuery.class))).thenReturn(quote);

    // Act
    ResponseEntity<BookingQuoteResource> response = controller.getBookingQuote(10L, 1L, 2);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(10L, response.getBody().availabilityId());
    assertEquals(new BigDecimal("80.00"), response.getBody().totalPrice());
    assertEquals("PEN", response.getBody().currency());
  }

  @Test
  void getBookingQuote_returnsBadRequest_whenIllegalArgumentThrown() {
    // Arrange
    when(bookingQuoteService.handle(any(GetBookingQuoteQuery.class)))
        .thenThrow(new IllegalArgumentException("Availability not found"));

    // Act
    ResponseEntity<BookingQuoteResource> response = controller.getBookingQuote(999L, 1L, 2);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void getBookingQuote_returnsBadRequest_whenQuantityInvalid() {
    // Act
    ResponseEntity<BookingQuoteResource> response = controller.getBookingQuote(10L, 1L, 0);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}