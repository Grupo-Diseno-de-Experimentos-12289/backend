package pe.edu.upc.travelmatch.bookings.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Payment;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Payout;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Refund;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingStatus;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.Instant;

@Entity
@Getter
public class Booking extends AuditableAbstractAggregateRoot<Booking> {

    @Embedded
    private UserId userId;

    @Embedded
    private AvailabilityId availabilityId;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Refund refund;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payout payout;

    private int quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_amount_value")),
            @AttributeOverride(name = "currency", column = @Column(name = "total_amount_currency"))
    })
    private Money totalBookingPrice;

    private BookingStatus bookingStatus;

    private Instant bookingDate;

    protected Booking() {
    }

    public Booking(UserId userId, AvailabilityId availabilityId, Money totalBookingPrice, int quantity, BookingStatus bookingStatus, Instant bookingDate) {
        this.userId = userId;
        this.availabilityId = availabilityId;
        this.totalBookingPrice = totalBookingPrice;
        this.quantity = quantity;
        this.bookingStatus = bookingStatus;
        this.bookingDate = bookingDate;
    }

    public void addPayment(Payment payment) {
        if (this.payment != null) {
            throw new IllegalStateException("This booking already has a payment associated.");
        }
        if (this.bookingStatus != BookingStatus.PENDING) {
            throw new IllegalStateException("Cannot add a payment to a booking that is not in PENDING status.");
        }
        this.payment = payment;
        payment.setBooking(this);
    }

    public void addRefund(Refund refund) {
        if (this.refund != null) {
            throw new IllegalStateException("This booking already has a refund associated.");
        }
        if (this.bookingStatus != BookingStatus.SUCCEEDED && this.bookingStatus != BookingStatus.CANCELLED) {
            throw new IllegalStateException("Refunds can only be associated to bookings in SUCCEEDED or CANCELLED status.");
        }
        this.refund = refund;
        refund.setBooking(this);
    }

    public void addPayout(Payout payout) {
        if (this.payout != null) {
            throw new IllegalStateException("This booking already has a payout associated.");
        }
        if (this.bookingStatus != BookingStatus.SUCCEEDED) {
            throw new IllegalStateException("Payouts can only be processed for bookings in SUCCEEDED status.");
        }
        this.payout = payout;
        payout.setBooking(this);
    }

    public void markAsSucceeded() {
        if (this.bookingStatus == BookingStatus.PENDING) {
            this.bookingStatus = BookingStatus.SUCCEEDED;
        } else {
            throw new IllegalStateException("Booking can only be marked as SUCCEEDED from PENDING state.");
        }
    }

    public void markAsCancelled() {
        if (this.bookingStatus == BookingStatus.PENDING) {
            this.bookingStatus = BookingStatus.CANCELLED;
        } else {
            throw new IllegalStateException("Booking can only be cancelled from PENDING state.");
        }
    }

    public void markAsFailed() {
        if (this.bookingStatus == BookingStatus.PENDING) {
            this.bookingStatus = BookingStatus.FAILED;
        } else {
            throw new IllegalStateException("Booking can only fail from PENDING state.");
        }
    }
}
