package pe.edu.upc.travelmatch.bookings.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.PaymentStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.TransactionId;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

import java.time.Instant;

@Entity
@Getter
public class Payment extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    @Setter
    private Booking booking;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "payment_amount_value")),
            @AttributeOverride(name = "currency", column = @Column(name = "payment_amount_currency"))
    })
    private Money paymentMoney;

    private String paymentMethod;

    @Embedded
    private TransactionId transactionId;

    private PaymentStatus paymentStatus;

    private Instant paymentDate;

    public Payment() {
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public Payment(Money paymentMoney, String paymentMethod, TransactionId transactionId, PaymentStatus paymentStatus, Instant paymentDate) {
        this.paymentMoney = paymentMoney;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public void markAsSucceeded() {
        if (this.paymentStatus == PaymentStatus.PENDING) {
            this.paymentStatus = PaymentStatus.SUCCEEDED;
        } else {
            throw new IllegalStateException("Payment can only be marked as SUCCEEDED from PENDING state.");
        }
    }

    public void markAsFailed() {
        if (this.paymentStatus == PaymentStatus.PENDING) {
            this.paymentStatus = PaymentStatus.FAILED;
        } else {
            throw new IllegalStateException("Payment can only fail from PENDING state.");
        }
    }

    public void markAsRefunded() {
        if (this.paymentStatus == PaymentStatus.SUCCEEDED) {
            this.paymentStatus = PaymentStatus.REFUNDED;
        } else {
            throw new IllegalStateException("Payment must be SUCCEEDED before it can be refunded.");
        }
    }
}
