package pe.edu.upc.travelmatch.bookings.domain.model.aggregates;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Payment;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Payout;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Refund;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.PaymentStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.PayoutStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.RefundStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.TransactionId;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.UserId;

@DisplayName("Booking Aggregate - Unit Tests")
class BookingAggregateTest {
  // ── shared fixtures ──────────────────────────────────────
  private Money totalPrice;
  private Booking booking;

  @BeforeEach
  void arrange_sharedBooking() {
    totalPrice = new Money(new BigDecimal("150.00"), "PEN");
    booking =
        new Booking(
            new UserId(1L),
            new AvailabilityId(10L),
            totalPrice,
            2,
            BookingStatus.PENDING,
            Instant.now());
  }

  @Nested
  @DisplayName("Status Transitions")
  class StatusTransitions {

    @Test
    @DisplayName("markAsSucceeded() changes status to SUCCEEDED when booking is PENDING")
    void markAsSucceeded_fromPending_statusIsSucceeded() {
      // Arrange – booking already PENDING (from @BeforeEach)

      // Act
      booking.markAsSucceeded();

      // Assert
      assertEquals(BookingStatus.SUCCEEDED, booking.getBookingStatus());
    }

    @Test
    @DisplayName("markAsSucceeded() throws when booking is already CANCELLED")
    void markAsSucceeded_fromCancelled_throwsIllegalState() {
      // Arrange
      booking.markAsCancelled();

      // Act & Assert
      assertThrows(IllegalStateException.class, booking::markAsSucceeded);
    }

    @Test
    @DisplayName("markAsCancelled() changes status to CANCELLED when booking is PENDING")
    void markAsCancelled_fromPending_statusIsCancelled() {
      // Arrange – booking PENDING

      // Act
      booking.markAsCancelled();

      // Assert
      assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
    }

    @Test
    @DisplayName("markAsCancelled() throws when booking is SUCCEEDED")
    void markAsCancelled_fromSucceeded_throwsIllegalState() {
      // Arrange
      booking.markAsSucceeded();

      // Act & Assert
      assertThrows(IllegalStateException.class, booking::markAsCancelled);
    }

    @Test
    @DisplayName("markAsFailed() changes status to FAILED when booking is PENDING")
    void markAsFailed_fromPending_statusIsFailed() {
      // Arrange – booking PENDING

      // Act
      booking.markAsFailed();

      // Assert
      assertEquals(BookingStatus.FAILED, booking.getBookingStatus());
    }

    @Test
    @DisplayName("markAsFailed() throws when booking is already CANCELLED")
    void markAsFailed_fromCancelled_throwsIllegalState() {
      // Arrange
      booking.markAsCancelled();

      // Act & Assert
      assertThrows(IllegalStateException.class, booking::markAsFailed);
    }

    @Test
    @DisplayName("New booking starts with PENDING status")
    void newBooking_statusIsPending() {
      // Arrange
      Booking newBooking =
          new Booking(
              new UserId(5L),
              new AvailabilityId(20L),
              totalPrice,
              1,
              BookingStatus.PENDING,
              Instant.now());

      // Act
      BookingStatus status = newBooking.getBookingStatus();

      // Assert
      assertEquals(BookingStatus.PENDING, status);
    }
  }

  // =========================================================
  //  PAYMENT ASSOCIATION
  // =========================================================
  @Nested
  @DisplayName("Payment Association")
  class PaymentAssociation {

    @Test
    @DisplayName("addPayment() links payment when booking is PENDING and has no payment")
    void addPayment_pendingBookingNoPayment_paymentIsLinked() {
      // Arrange
      Payment payment = buildPayment("pi_first_001");

      // Act
      booking.addPayment(payment);

      // Assert
      assertNotNull(booking.getPayment());
      assertEquals(payment, booking.getPayment());
    }

    @Test
    @DisplayName("addPayment() sets back-reference booking on payment entity")
    void addPayment_setsBookingBackReference() {
      // Arrange
      Payment payment = buildPayment("pi_ref_001");

      // Act
      booking.addPayment(payment);

      // Assert
      assertEquals(booking, payment.getBooking());
    }

    @Test
    @DisplayName("addPayment() throws when a payment already exists on the booking")
    void addPayment_alreadyHasPayment_throwsIllegalState() {
      // Arrange
      Payment first = buildPayment("pi_first_001");
      Payment second = buildPayment("pi_second_002");
      booking.addPayment(first);

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> booking.addPayment(second));
    }

    @Test
    @DisplayName("addPayment() throws when booking is not in PENDING status")
    void addPayment_bookingSucceeded_throwsIllegalState() {
      // Arrange
      booking.markAsSucceeded();
      Payment latePayment = buildPayment("pi_late_003");

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> booking.addPayment(latePayment));
    }

    private Payment buildPayment(String txnId) {
      return new Payment(
          totalPrice, "card", new TransactionId(txnId), PaymentStatus.PENDING, Instant.now());
    }
  }

  // =========================================================
  //  REFUND ASSOCIATION
  // =========================================================
  @Nested
  @DisplayName("Refund Association")
  class RefundAssociation {

    @Test
    @DisplayName("addRefund() links refund when booking is SUCCEEDED")
    void addRefund_succeededBooking_refundIsLinked() {
      // Arrange
      booking.markAsSucceeded();
      Refund refund = buildRefund("Customer request");

      // Act
      booking.addRefund(refund);

      // Assert
      assertNotNull(booking.getRefund());
    }

    @Test
    @DisplayName("addRefund() links refund when booking is CANCELLED")
    void addRefund_cancelledBooking_refundIsLinked() {
      // Arrange
      booking.markAsCancelled();
      Refund refund = buildRefund("Cancellation refund");

      // Act
      booking.addRefund(refund);

      // Assert
      assertNotNull(booking.getRefund());
    }

    @Test
    @DisplayName("addRefund() throws when booking is still PENDING")
    void addRefund_pendingBooking_throwsIllegalState() {
      // Arrange
      Refund refund = buildRefund("Too early");

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> booking.addRefund(refund));
    }

    @Test
    @DisplayName("addRefund() throws when a refund already exists")
    void addRefund_alreadyHasRefund_throwsIllegalState() {
      // Arrange
      booking.markAsSucceeded();
      Refund first = buildRefund("First refund");
      Refund second = buildRefund("Second refund");
      booking.addRefund(first);

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> booking.addRefund(second));
    }

    private Refund buildRefund(String reason) {
      return new Refund(totalPrice, reason, RefundStatus.PENDING, Instant.now());
    }
  }

  // =========================================================
  //  PAYOUT ASSOCIATION
  // =========================================================
  @Nested
  @DisplayName("Payout Association")
  class PayoutAssociation {

    @Test
    @DisplayName("addPayout() links payout when booking is SUCCEEDED")
    void addPayout_succeededBooking_payoutIsLinked() {
      // Arrange
      booking.markAsSucceeded();
      Payout payout = buildPayout();

      // Act
      booking.addPayout(payout);

      // Assert
      assertNotNull(booking.getPayout());
    }

    @Test
    @DisplayName("addPayout() throws when booking is PENDING")
    void addPayout_pendingBooking_throwsIllegalState() {
      // Arrange
      Payout payout = buildPayout();

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> booking.addPayout(payout));
    }

    @Test
    @DisplayName("addPayout() throws when a payout already exists")
    void addPayout_alreadyHasPayout_throwsIllegalState() {
      // Arrange
      booking.markAsSucceeded();
      Payout first = buildPayout();
      Payout second = buildPayout();
      booking.addPayout(first);

      // Act & Assert
      assertThrows(IllegalStateException.class, () -> booking.addPayout(second));
    }

    private Payout buildPayout() {
      return new Payout(
          5L, totalPrice, BigDecimal.valueOf(0.10), PayoutStatus.PENDING, Instant.now());
    }
  }

  // =========================================================
  //  FIELD GETTERS
  // =========================================================
  @Nested
  @DisplayName("Field Getters")
  class FieldGetters {

    @Test
    @DisplayName("getUserId() returns the userId used at construction")
    void getUserId_returnsExpectedUserId() {
      // Arrange – booking from @BeforeEach

      // Act
      Long userId = booking.getUserId().userId();

      // Assert
      assertEquals(1L, userId);
    }

    @Test
    @DisplayName("getAvailabilityId() returns the availabilityId used at construction")
    void getAvailabilityId_returnsExpectedId() {
      // Arrange – booking from @BeforeEach

      // Act
      Long availabilityId = booking.getAvailabilityId().availabilityId();

      // Assert
      assertEquals(10L, availabilityId);
    }

    @Test
    @DisplayName("getQuantity() returns the quantity used at construction")
    void getQuantity_returnsExpectedQuantity() {
      // Arrange – booking from @BeforeEach

      // Act
      int quantity = booking.getQuantity();

      // Assert
      assertEquals(2, quantity);
    }

    @Test
    @DisplayName("getTotalBookingPrice() returns correct amount and currency")
    void getTotalBookingPrice_returnsExpectedMoney() {
      // Arrange – booking from @BeforeEach

      // Act
      Money price = booking.getTotalBookingPrice();

      // Assert
      assertAll(
          () -> assertEquals(new BigDecimal("150.00"), price.getAmount()),
          () -> assertEquals("PEN", price.getCurrency()));
    }
  }
}
