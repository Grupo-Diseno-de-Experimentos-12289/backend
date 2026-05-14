package pe.edu.upc.travelmatch.bookings.domain.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.RefundStatus;

@Nested
@DisplayName("Refund Entity - Status Transitions")
class RefundEntityTests {
  private static final Money MONEY = new Money(new BigDecimal("100.00"), "PEN");

  @Test
  @DisplayName("new Refund() defaults to PENDING status")
  void newRefund_defaultStatus_isPending() {
    // Arrange

    // Act
    Refund refund = new Refund();

    // Assert
    assertEquals(RefundStatus.PENDING, refund.getRefundStatus());
  }

  @Test
  @DisplayName("markAsSucceeded() changes status to SUCCEEDED from PENDING")
  void markAsSucceeded_fromPending_statusIsSucceeded() {
    // Arrange
    Refund refund = buildRefund();

    // Act
    refund.markAsSucceeded();

    // Assert
    assertEquals(RefundStatus.SUCCEEDED, refund.getRefundStatus());
  }

  @Test
  @DisplayName("markAsSucceeded() throws when already SUCCEEDED")
  void markAsSucceeded_alreadySucceeded_throwsIllegalState() {
    // Arrange
    Refund refund = buildRefund();
    refund.markAsSucceeded();

    // Act & Assert
    assertThrows(IllegalStateException.class, refund::markAsSucceeded);
  }

  @Test
  @DisplayName("markAsFailed() changes status to FAILED from PENDING")
  void markAsFailed_fromPending_statusIsFailed() {
    // Arrange
    Refund refund = buildRefund();

    // Act
    refund.markAsFailed();

    // Assert
    assertEquals(RefundStatus.FAILED, refund.getRefundStatus());
  }

  @Test
  @DisplayName("markAsCancelled() changes status to CANCELLED from PENDING")
  void markAsCancelled_fromPending_statusIsCancelled() {
    // Arrange
    Refund refund = buildRefund();

    // Act
    refund.markAsCancelled();

    // Assert
    assertEquals(RefundStatus.CANCELLED, refund.getRefundStatus());
  }

  @Test
  @DisplayName("markAsCancelled() throws when already CANCELLED")
  void markAsCancelled_alreadyCancelled_throwsIllegalState() {
    // Arrange
    Refund refund = buildRefund();
    refund.markAsCancelled();

    // Act & Assert
    assertThrows(IllegalStateException.class, refund::markAsCancelled);
  }

  private Refund buildRefund() {
    return new Refund(MONEY, "Test reason", RefundStatus.PENDING, Instant.now());
  }
}
