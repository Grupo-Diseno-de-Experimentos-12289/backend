package pe.edu.upc.travelmatch.bookings.domain.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.PayoutStatus;

@Nested
@DisplayName("Payout Entity - Status Transitions")
class PayoutEntityTests {
  private static final Money MONEY = new Money(new BigDecimal("100.00"), "PEN");

  @Test
  @DisplayName("new Payout() defaults to PENDING status")
  void newPayout_defaultStatus_isPending() {
    // Arrange

    // Act
    Payout payout = new Payout();

    // Assert
    assertEquals(PayoutStatus.PENDING, payout.getPayoutStatus());
  }

  @Test
  @DisplayName("markAsSucceeded() changes status to SUCCEEDED from PENDING")
  void markAsSucceeded_fromPending_statusIsSucceeded() {
    // Arrange
    Payout payout = buildPayout();

    // Act
    payout.markAsSucceeded();

    // Assert
    assertEquals(PayoutStatus.SUCCEEDED, payout.getPayoutStatus());
  }

  @Test
  @DisplayName("markAsSucceeded() throws when already SUCCEEDED")
  void markAsSucceeded_alreadySucceeded_throwsIllegalState() {
    // Arrange
    Payout payout = buildPayout();
    payout.markAsSucceeded();

    // Act & Assert
    assertThrows(IllegalStateException.class, payout::markAsSucceeded);
  }

  @Test
  @DisplayName("markAsFailed() changes status to FAILED from PENDING")
  void markAsFailed_fromPending_statusIsFailed() {
    // Arrange
    Payout payout = buildPayout();

    // Act
    payout.markAsFailed();

    // Assert
    assertEquals(PayoutStatus.FAILED, payout.getPayoutStatus());
  }

  @Test
  @DisplayName("markAsCancelled() changes status to CANCELLED from PENDING")
  void markAsCancelled_fromPending_statusIsCancelled() {
    // Arrange
    Payout payout = buildPayout();

    // Act
    payout.markAsCancelled();

    // Assert
    assertEquals(PayoutStatus.CANCELLED, payout.getPayoutStatus());
  }

  @Test
  @DisplayName("Payout stores agencyId correctly")
  void payout_storesAgencyId() {
    // Arrange
    Payout payout = buildPayout();

    // Act
    Long agencyId = payout.getAgencyId();

    // Assert
    assertEquals(5L, agencyId);
  }

  private Payout buildPayout() {
    return new Payout(5L, MONEY, BigDecimal.valueOf(0.10), PayoutStatus.PENDING, Instant.now());
  }
}
