package pe.edu.upc.travelmatch.bookings.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.RefundStatus;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

import java.time.Instant;

@Entity
@Getter
public class Refund extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    @Setter
    private Booking booking;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "refund_amount_value")),
            @AttributeOverride(name = "currency", column = @Column(name = "refund_amount_currency"))
    })
    private Money refundMoney;

    private String refundReason;

    private RefundStatus refundStatus;

    private Instant refundDate;

    public Refund() {
        this.refundStatus = RefundStatus.PENDING;
    }

    public Refund(Money refundMoney, String refundReason, RefundStatus refundStatus, Instant refundDate) {
        this.refundMoney = refundMoney;
        this.refundReason = refundReason;
        this.refundStatus = refundStatus;
        this.refundDate = refundDate;
    }

    public void markAsSucceeded() {
        if (this.refundStatus == RefundStatus.PENDING) {
            this.refundStatus = RefundStatus.SUCCEEDED;
        } else {
            throw new IllegalStateException("Refund can only succeed from PENDING state.");
        }
    }

    public void markAsFailed() {
        if (this.refundStatus == RefundStatus.PENDING) {
            this.refundStatus = RefundStatus.FAILED;
        } else {
            throw new IllegalStateException("Refund can only fail from PENDING state.");
        }
    }

    public void markAsCancelled() {
        if (this.refundStatus == RefundStatus.PENDING) {
            this.refundStatus = RefundStatus.CANCELLED;
        } else {
            throw new IllegalStateException("Refund can only be cancelled from PENDING state.");
        }
    }
}
