package pe.edu.upc.travelmatch.bookings.domain.model.entities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Payment;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Payout;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Refund;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.*;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("Payment Entity - Status Transitions")
public class PaymentEntityTests {
    private static final Money MONEY = new Money(new BigDecimal("100.00"), "PEN");
    @Test
    @DisplayName("new Payment() defaults to PENDING status")
    void newPayment_defaultStatus_isPending() {
        // Arrange

        // Act
        Payment payment = new Payment();

        // Assert
        assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("markAsSucceeded() changes status to SUCCEEDED from PENDING")
    void markAsSucceeded_fromPending_statusIsSucceeded() {
        // Arrange
        Payment payment = buildPayment(PaymentStatus.PENDING);

        // Act
        payment.markAsSucceeded();

        // Assert
        assertEquals(PaymentStatus.SUCCEEDED, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("markAsSucceeded() throws when status is already SUCCEEDED")
    void markAsSucceeded_alreadySucceeded_throwsIllegalState() {
        // Arrange
        Payment payment = buildPayment(PaymentStatus.PENDING);
        payment.markAsSucceeded();

        // Act & Assert
        assertThrows(IllegalStateException.class, payment::markAsSucceeded);
    }

    @Test
    @DisplayName("markAsFailed() changes status to FAILED from PENDING")
    void markAsFailed_fromPending_statusIsFailed() {
        // Arrange
        Payment payment = buildPayment(PaymentStatus.PENDING);

        // Act
        payment.markAsFailed();

        // Assert
        assertEquals(PaymentStatus.FAILED, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("markAsFailed() throws when status is already FAILED")
    void markAsFailed_alreadyFailed_throwsIllegalState() {
        // Arrange
        Payment payment = buildPayment(PaymentStatus.PENDING);
        payment.markAsFailed();

        // Act & Assert
        assertThrows(IllegalStateException.class, payment::markAsFailed);
    }

    @Test
    @DisplayName("markAsRefunded() changes status to REFUNDED from SUCCEEDED")
    void markAsRefunded_fromSucceeded_statusIsRefunded() {
        // Arrange
        Payment payment = buildPayment(PaymentStatus.PENDING);
        payment.markAsSucceeded();

        // Act
        payment.markAsRefunded();

        // Assert
        assertEquals(PaymentStatus.REFUNDED, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("markAsRefunded() throws when status is PENDING (not SUCCEEDED)")
    void markAsRefunded_fromPending_throwsIllegalState() {
        // Arrange
        Payment payment = buildPayment(PaymentStatus.PENDING);

        // Act & Assert
        assertThrows(IllegalStateException.class, payment::markAsRefunded);
    }

    private Payment buildPayment(PaymentStatus status) {
        return new Payment(MONEY, "card", new TransactionId("pi_001"), status, Instant.now());
    }
}
