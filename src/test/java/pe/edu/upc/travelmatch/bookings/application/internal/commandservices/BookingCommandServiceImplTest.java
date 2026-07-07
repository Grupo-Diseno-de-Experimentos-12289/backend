package pe.edu.upc.travelmatch.bookings.application.internal.commandservices;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.bookings.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.bookings.application.internal.paymentgateway.StripePaymentGatewayAdapter;
import pe.edu.upc.travelmatch.bookings.application.internal.paymentgateway.StripePaymentIntentResponse;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.CancelBookingCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.CreateBookingCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.FailPaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.InitiatePaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.InitiateRefundCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.ProcessPaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Refund;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.RefundStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.TransactionId;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingCommandServiceImpl - Unit Tests")
class BookingCommandServiceImplTest {
  // ── mocks ────────────────────────────────────────────────
  @Mock private BookingRepository bookingRepository;
  @Mock private ExternalExperienceService externalExperienceService;
  @Mock private StripePaymentGatewayAdapter paymentGatewayAdapter;

  @InjectMocks private BookingCommandServiceImpl bookingCommandService;

  // ── shared fixtures ──────────────────────────────────────
  private Money totalPrice;
  private Booking pendingBooking;

  @BeforeEach
  void arrange_sharedFixtures() {
    totalPrice = new Money(new BigDecimal("150.00"), "PEN");
    pendingBooking =
        new Booking(
            new UserId(1L),
            new AvailabilityId(10L),
            totalPrice,
            2,
            BookingStatus.PENDING,
            Instant.now());
  }

  // =========================================================
  //  CREATE BOOKING
  // =========================================================
  @Nested
  @DisplayName("CreateBooking command")
  class CreateBookingTests {

    @Test
    @DisplayName("handle(CreateBookingCommand) saves booking when availability and stock are valid")
    void handle_validCommand_savesBookingAndDecrementsStock() {
      // Arrange
      final CreateBookingCommand command = new CreateBookingCommand(1L, 10L, 1L, 2, Instant.now());

      when(externalExperienceService.existsAvailabilityById(10L)).thenReturn(true);
      when(externalExperienceService.hasSufficientStock(10L, 1L, 2)).thenReturn(true);
      when(externalExperienceService.getPriceForTicketType(10L, 1L))
          .thenReturn(new BigDecimal("75.00"));
      when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

      // Act
      assertDoesNotThrow(() -> bookingCommandService.handle(command));

      // Assert
      verify(externalExperienceService).decrementStock(10L, 1L, 2);
      verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    @DisplayName("handle(CreateBookingCommand) calculates totalPrice = unitPrice x quantity")
    void handle_validCommand_computesTotalPriceCorrectly() {
      // Arrange
      final CreateBookingCommand command = new CreateBookingCommand(1L, 10L, 1L, 3, Instant.now());

      when(externalExperienceService.existsAvailabilityById(10L)).thenReturn(true);
      when(externalExperienceService.hasSufficientStock(10L, 1L, 3)).thenReturn(true);
      when(externalExperienceService.getPriceForTicketType(10L, 1L))
          .thenReturn(new BigDecimal("50.00"));

      ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
      when(bookingRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

      // Act
      bookingCommandService.handle(command);

      // Assert
      Booking saved = captor.getValue();
      assertEquals(new BigDecimal("150.00"), saved.getTotalBookingPrice().getAmount());
    }

    @Test
    @DisplayName(
        "handle(CreateBookingCommand) throws IllegalArgumentException when availability not found")
    void handle_availabilityNotFound_throwsIllegalArgument() {
      // Arrange
      CreateBookingCommand command = new CreateBookingCommand(1L, 99L, 1L, 2, Instant.now());

      when(externalExperienceService.existsAvailabilityById(99L)).thenReturn(false);

      // Act & Assert
      assertThrows(IllegalArgumentException.class, () -> bookingCommandService.handle(command));

      verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName(
        "handle(CreateBookingCommand) throws IllegalStateException when stock is insufficient")
    void handle_insufficientStock_throwsIllegalState() {
      // Arrange
      CreateBookingCommand command = new CreateBookingCommand(1L, 10L, 1L, 100, Instant.now());

      when(externalExperienceService.existsAvailabilityById(10L)).thenReturn(true);
      when(externalExperienceService.hasSufficientStock(10L, 1L, 100)).thenReturn(false);

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> bookingCommandService.handle(command));

      verify(bookingRepository, never()).save(any());
    }
  }

  // =========================================================
  //  CANCEL BOOKING
  // =========================================================
  @Nested
  @DisplayName("CancelBooking command")
  class CancelBookingTests {

    @Test
    @DisplayName("handle(CancelBookingCommand) cancels PENDING booking and returns it")
    void handle_pendingBooking_returnsCancelledBooking() {
      // Arrange
      CancelBookingCommand command = new CancelBookingCommand(1L, 1L, "No longer needed");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));
      when(bookingRepository.save(pendingBooking)).thenReturn(pendingBooking);

      // Act
      Optional<Booking> result = bookingCommandService.handle(command);

      // Assert
      assertTrue(result.isPresent());
      assertEquals(BookingStatus.CANCELLED, result.get().getBookingStatus());
    }

    @Test
    @DisplayName("handle(CancelBookingCommand) returns empty Optional when booking not found")
    void handle_bookingNotFound_returnsEmpty() {
      // Arrange
      CancelBookingCommand command = new CancelBookingCommand(999L, 1L, "reason");
      when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

      // Act
      Optional<Booking> result = bookingCommandService.handle(command);

      // Assert
      assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("handle(CancelBookingCommand) throws when booking is already SUCCEEDED")
    void handle_succeededBooking_throwsIllegalState() {
      // Arrange
      pendingBooking.markAsSucceeded();
      CancelBookingCommand command = new CancelBookingCommand(1L, 1L, "reason");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> bookingCommandService.handle(command));
    }
  }

  // =========================================================
  //  INITIATE PAYMENT
  // =========================================================
  @Nested
  @DisplayName("InitiatePayment command")
  class InitiatePaymentTests {

    @Test
    @DisplayName("handle(InitiatePaymentCommand) returns client secret from Stripe")
    void handle_validPendingBooking_returnsClientSecret() {
      // Arrange
      InitiatePaymentCommand command = new InitiatePaymentCommand(1L, "card");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));
      when(paymentGatewayAdapter.processTransaction(eq("card"), anyLong(), any()))
          .thenReturn(new StripePaymentIntentResponse("pi_test", "secret_test"));

      // Act
      String clientSecret = bookingCommandService.handle(command);

      // Assert
      assertEquals("secret_test", clientSecret);
    }

    @Test
    @DisplayName(
        "handle(InitiatePaymentCommand) throws IllegalArgumentException when booking not found")
    void handle_bookingNotFound_throwsIllegalArgument() {
      // Arrange
      InitiatePaymentCommand command = new InitiatePaymentCommand(999L, "card");
      when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThrows(IllegalArgumentException.class, () -> bookingCommandService.handle(command));
    }

    @Test
    @DisplayName("handle(InitiatePaymentCommand) throws when booking status is CANCELLED")
    void handle_cancelledBooking_throwsIllegalState() {
      // Arrange
      pendingBooking.markAsCancelled();
      InitiatePaymentCommand command = new InitiatePaymentCommand(1L, "card");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> bookingCommandService.handle(command));
    }

    @Test
    @DisplayName("handle(InitiatePaymentCommand) throws when Stripe returns null")
    void handle_stripeReturnsNull_throwsIllegalState() {
      // Arrange
      InitiatePaymentCommand command = new InitiatePaymentCommand(1L, "card");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));
      when(paymentGatewayAdapter.processTransaction(anyString(), anyLong(), any()))
          .thenReturn(null);

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> bookingCommandService.handle(command));
    }
  }

  // =========================================================
  //  PROCESS PAYMENT
  // =========================================================
  @Nested
  @DisplayName("ProcessPayment command")
  class ProcessPaymentTests {

    @Test
    @DisplayName("handle(ProcessPaymentCommand) marks booking as SUCCEEDED and saves")
    void handle_pendingBooking_bookingMarkedSucceeded() {
      // Arrange
      ProcessPaymentCommand command =
          new ProcessPaymentCommand(1L, "card", new TransactionId("pi_test_001"));
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));
      when(bookingRepository.save(pendingBooking)).thenReturn(pendingBooking);

      // Act
      bookingCommandService.handle(command);

      // Assert
      assertEquals(BookingStatus.SUCCEEDED, pendingBooking.getBookingStatus());
      verify(bookingRepository).save(pendingBooking);
    }

    @Test
    @DisplayName(
        "handle(ProcessPaymentCommand) throws IllegalArgumentException when booking not found")
    void handle_bookingNotFound_throwsIllegalArgument() {
      // Arrange
      ProcessPaymentCommand command =
          new ProcessPaymentCommand(999L, "card", new TransactionId("pi_test_001"));
      when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThrows(IllegalArgumentException.class, () -> bookingCommandService.handle(command));
    }

    @Test
    @DisplayName("handle(ProcessPaymentCommand) throws when booking is not PENDING")
    void handle_nonPendingBooking_throwsIllegalState() {
      // Arrange
      pendingBooking.markAsCancelled();
      ProcessPaymentCommand command =
          new ProcessPaymentCommand(1L, "card", new TransactionId("pi_test_001"));
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> bookingCommandService.handle(command));
    }
  }

  // =========================================================
  //  FAIL PAYMENT
  // =========================================================
  @Nested
  @DisplayName("FailPayment command")
  class FailPaymentTests {

    @Test
    @DisplayName("handle(FailPaymentCommand) marks booking as FAILED and returns true")
    void handle_pendingBooking_bookingMarkedFailedReturnsTrue() {
      // Arrange
      FailPaymentCommand command = new FailPaymentCommand(1L, "Insufficient funds");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));
      when(bookingRepository.save(pendingBooking)).thenReturn(pendingBooking);

      // Act
      boolean result = bookingCommandService.handle(command);

      // Assert
      assertTrue(result);
      assertEquals(BookingStatus.FAILED, pendingBooking.getBookingStatus());
    }

    @Test
    @DisplayName(
        "handle(FailPaymentCommand) throws IllegalArgumentException when booking not found")
    void handle_bookingNotFound_throwsIllegalArgument() {
      // Arrange
      FailPaymentCommand command = new FailPaymentCommand(999L, "reason");
      when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThrows(IllegalArgumentException.class, () -> bookingCommandService.handle(command));
    }

    @Test
    @DisplayName("handle(FailPaymentCommand) throws when booking is already SUCCEEDED")
    void handle_succeededBooking_throwsIllegalState() {
      // Arrange
      pendingBooking.markAsSucceeded();
      FailPaymentCommand command = new FailPaymentCommand(1L, "Late failure");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> bookingCommandService.handle(command));
    }
  }

  // =========================================================
  //  INITIATE REFUND
  // =========================================================
  @Nested
  @DisplayName("InitiateRefund command")
  class InitiateRefundTests {

    @Test
    @DisplayName("handle(InitiateRefundCommand) creates refund for SUCCEEDED booking")
    void handle_succeededBooking_refundCreated() {
      // Arrange
      pendingBooking.markAsSucceeded();
      InitiateRefundCommand command = new InitiateRefundCommand(1L, "Customer request");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));
      when(bookingRepository.save(pendingBooking)).thenReturn(pendingBooking);

      // Act
      bookingCommandService.handle(command);

      // Assert
      assertNotNull(pendingBooking.getRefund());
      verify(bookingRepository).save(pendingBooking);
    }

    @Test
    @DisplayName("handle(InitiateRefundCommand) creates refund for CANCELLED booking")
    void handle_cancelledBooking_refundCreated() {
      // Arrange
      pendingBooking.markAsCancelled();
      InitiateRefundCommand command = new InitiateRefundCommand(1L, "Cancellation");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));
      when(bookingRepository.save(pendingBooking)).thenReturn(pendingBooking);

      // Act
      bookingCommandService.handle(command);

      // Assert
      assertNotNull(pendingBooking.getRefund());
    }

    @Test
    @DisplayName("handle(InitiateRefundCommand) throws when booking is PENDING")
    void handle_pendingBooking_throwsIllegalState() {
      // Arrange
      InitiateRefundCommand command = new InitiateRefundCommand(1L, "Too early");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> bookingCommandService.handle(command));
    }

    @Test
    @DisplayName("handle(InitiateRefundCommand) throws when refund already exists")
    void handle_refundAlreadyExists_throwsIllegalState() {
      // Arrange
      pendingBooking.markAsSucceeded();
      pendingBooking.addRefund(
          new Refund(totalPrice, "First refund", RefundStatus.PENDING, Instant.now()));
      InitiateRefundCommand command = new InitiateRefundCommand(1L, "Second refund");
      when(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking));

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> bookingCommandService.handle(command));
    }

    @Test
    @DisplayName(
        "handle(InitiateRefundCommand) throws IllegalArgumentException when booking not found")
    void handle_bookingNotFound_throwsIllegalArgument() {
      // Arrange
      InitiateRefundCommand command = new InitiateRefundCommand(999L, "reason");
      when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThrows(IllegalArgumentException.class, () -> bookingCommandService.handle(command));
    }
  }
}
