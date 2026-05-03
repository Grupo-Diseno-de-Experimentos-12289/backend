package pe.edu.upc.travelmatch.bookings.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.PayoutStatus;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
public class Payout extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    @Setter
    private Booking booking;

    @Getter
    private Long agencyId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "payout_amount_value")),
            @AttributeOverride(name = "currency", column = @Column(name = "payout_amount_currency"))
    })
    private Money payoutMoney;

    private BigDecimal payoutFee;

    private PayoutStatus payoutStatus;

    private Instant payoutDate;

    public Payout() {
        this.payoutStatus = PayoutStatus.PENDING;
    }

    public Payout(Long agencyId, Money payoutMoney, BigDecimal payoutFee, PayoutStatus payoutStatus, Instant payoutDate) {
        this.agencyId = agencyId;
        this.payoutMoney = payoutMoney;
        this.payoutFee = payoutFee;
        this.payoutStatus = payoutStatus;
        this.payoutDate = payoutDate;
    }

    public void markAsSucceeded() {
        if (this.payoutStatus == PayoutStatus.PENDING) {
            this.payoutStatus = PayoutStatus.SUCCEEDED;
        } else {
            throw new IllegalStateException("Payout can only succeed from PENDING state.");
        }
    }

    public void markAsFailed() {
        if (this.payoutStatus == PayoutStatus.PENDING) {
            this.payoutStatus = PayoutStatus.FAILED;
        } else {
            throw new IllegalStateException("Payout can only fail from PENDING state.");
        }
    }

    public void markAsCancelled() {
        if (this.payoutStatus == PayoutStatus.PENDING) {
            this.payoutStatus = PayoutStatus.CANCELLED;
        } else {
            throw new IllegalStateException("Payout can only be cancelled from PENDING state.");
        }
    }
}
