package pe.edu.upc.travelmatch.bookings.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.CancelBookingCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.CreateBookingCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.FailPaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.InitiatePaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.InitiateRefundCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Refund;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingByIdQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.RefundStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingCommandService;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingQueryService;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.BookingResource;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.CancelBookingResource;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.CreateBookingResource;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.FailPaymentResource;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.InitiatePaymentResource;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.InitiateRefundResource;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.RefundResource;

@ExtendWith(MockitoExtension.class)
class BookingsControllerTest {

  @Mock private BookingCommandService commandService;
  @Mock private BookingQueryService queryService;
  @InjectMocks private BookingsController controller;

  private Booking booking;

  @BeforeEach
  void setUp() {
    booking =
        new Booking(
            new UserId(1L),
            new AvailabilityId(10L),
            new Money(new BigDecimal("100.00"), "PEN"),
            2,
            BookingStatus.PENDING,
            Instant.parse("2026-01-01T10:00:00Z"));
  }

  @Test
  void createBooking_returnsCreated_whenSuccess() {
    CreateBookingResource resource =
        new CreateBookingResource(1L, 10L, 1L, 2, Instant.parse("2026-01-01T10:00:00Z"));
    when(commandService.handle(any(CreateBookingCommand.class))).thenReturn(1L);
    when(queryService.handle(any(GetBookingByIdQuery.class))).thenReturn(Optional.of(booking));

    ResponseEntity<BookingResource> response = controller.createBooking(resource);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().quantity());
  }

  @Test
  void createBooking_returnsNotFound_whenBookingNotRetrieved() {
    CreateBookingResource resource =
        new CreateBookingResource(1L, 10L, 1L, 2, Instant.parse("2026-01-01T10:00:00Z"));
    when(commandService.handle(any(CreateBookingCommand.class))).thenReturn(1L);
    when(queryService.handle(any(GetBookingByIdQuery.class))).thenReturn(Optional.empty());

    ResponseEntity<BookingResource> response = controller.createBooking(resource);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void createBooking_returnsBadRequest_whenIllegalArgumentThrown() {
    CreateBookingResource resource =
        new CreateBookingResource(1L, 10L, 1L, 2, Instant.parse("2026-01-01T10:00:00Z"));
    when(commandService.handle(any(CreateBookingCommand.class)))
        .thenThrow(new IllegalArgumentException("Invalid"));

    ResponseEntity<BookingResource> response = controller.createBooking(resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void createBooking_returnsBadRequest_whenIllegalStateThrown() {
    CreateBookingResource resource =
        new CreateBookingResource(1L, 10L, 1L, 2, Instant.parse("2026-01-01T10:00:00Z"));
    when(commandService.handle(any(CreateBookingCommand.class)))
        .thenThrow(new IllegalStateException("Invalid state"));

    ResponseEntity<BookingResource> response = controller.createBooking(resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void cancelBooking_returnsOk_whenSuccess() {
    CancelBookingResource resource = new CancelBookingResource(1L, "Change of plans");
    when(commandService.handle(any(CancelBookingCommand.class))).thenReturn(Optional.of(booking));

    ResponseEntity<BookingResource> response = controller.cancelBooking(1L, resource);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void cancelBooking_returnsNotFound_whenServiceReturnsEmpty() {
    CancelBookingResource resource = new CancelBookingResource(1L, "Change of plans");
    when(commandService.handle(any(CancelBookingCommand.class))).thenReturn(Optional.empty());

    ResponseEntity<BookingResource> response = controller.cancelBooking(1L, resource);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void cancelBooking_returnsBadRequest_whenIllegalArgumentThrown() {
    CancelBookingResource resource = new CancelBookingResource(1L, "Change of plans");
    when(commandService.handle(any(CancelBookingCommand.class)))
        .thenThrow(new IllegalArgumentException("Invalid"));

    ResponseEntity<BookingResource> response = controller.cancelBooking(1L, resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void initiateRefund_returnsCreated_whenSuccess() {
    InitiateRefundResource resource = new InitiateRefundResource("Customer requested");
    Refund refund = mock(Refund.class);
    Booking bookingWithRefund = mock(Booking.class);
    Money refundMoney = new Money(new BigDecimal("100.00"), "PEN");
    when(commandService.handle(any(InitiateRefundCommand.class))).thenReturn(1L);
    when(queryService.handle(any(GetBookingByIdQuery.class)))
        .thenReturn(Optional.of(bookingWithRefund));
    when(bookingWithRefund.getRefund()).thenReturn(refund);
    when(refund.getId()).thenReturn(1L);
    when(refund.getBooking()).thenReturn(bookingWithRefund);
    when(bookingWithRefund.getId()).thenReturn(1L);
    when(refund.getRefundMoney()).thenReturn(refundMoney);
    when(refund.getRefundStatus()).thenReturn(RefundStatus.PENDING);
    when(refund.getRefundReason()).thenReturn("Customer requested");
    when(refund.getRefundDate()).thenReturn(Instant.parse("2026-01-01T10:00:00Z"));

    ResponseEntity<RefundResource> response = controller.initiateRefund(1L, resource);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void initiateRefund_returnsNotFound_whenBookingMissing() {
    InitiateRefundResource resource = new InitiateRefundResource("Customer requested");
    when(commandService.handle(any(InitiateRefundCommand.class))).thenReturn(1L);
    when(queryService.handle(any(GetBookingByIdQuery.class))).thenReturn(Optional.empty());

    ResponseEntity<RefundResource> response = controller.initiateRefund(1L, resource);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void initiateRefund_returnsNotFound_whenRefundMissing() {
    InitiateRefundResource resource = new InitiateRefundResource("Customer requested");
    Booking bookingWithoutRefund = mock(Booking.class);
    when(commandService.handle(any(InitiateRefundCommand.class))).thenReturn(1L);
    when(queryService.handle(any(GetBookingByIdQuery.class)))
        .thenReturn(Optional.of(bookingWithoutRefund));
    when(bookingWithoutRefund.getRefund()).thenReturn(null);

    ResponseEntity<RefundResource> response = controller.initiateRefund(1L, resource);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void initiateRefund_returnsBadRequest_whenIllegalArgumentThrown() {
    InitiateRefundResource resource = new InitiateRefundResource("Customer requested");
    when(commandService.handle(any(InitiateRefundCommand.class)))
        .thenThrow(new IllegalArgumentException("Invalid"));

    ResponseEntity<RefundResource> response = controller.initiateRefund(1L, resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void initiatePayment_returnsBadRequest_whenIdMismatch() {
    InitiatePaymentResource resource = new InitiatePaymentResource(2L, "card");

    ResponseEntity<String> response = controller.initiatePayment(1L, resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void initiatePayment_returnsOkWithClientSecret_whenSuccess() {
    InitiatePaymentResource resource = new InitiatePaymentResource(1L, "card");
    when(commandService.handle(any(InitiatePaymentCommand.class))).thenReturn("secret_xyz");

    ResponseEntity<String> response = controller.initiatePayment(1L, resource);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("secret_xyz", response.getBody());
  }

  @Test
  void failPayment_returnsOk_whenSuccess() {
    FailPaymentResource resource = new FailPaymentResource(1L, "card_declined");
    when(commandService.handle(any(FailPaymentCommand.class))).thenReturn(true);

    ResponseEntity<Void> response = controller.failPayment(resource);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void failPayment_returnsBadRequest_whenFailureReported() {
    FailPaymentResource resource = new FailPaymentResource(1L, "card_declined");
    when(commandService.handle(any(FailPaymentCommand.class))).thenReturn(false);

    ResponseEntity<Void> response = controller.failPayment(resource);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
