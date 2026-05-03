package pe.edu.upc.travelmatch.bookings.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.bookings.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.bookings.application.internal.paymentgateway.StripePaymentGatewayAdapter;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.*;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Payment;
import pe.edu.upc.travelmatch.bookings.domain.model.entities.Refund;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.*;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingCommandService;
import pe.edu.upc.travelmatch.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
public class BookingCommandServiceImpl implements BookingCommandService {
    private final BookingRepository bookingRepository;
    private final ExternalExperienceService externalExperienceService;
    private final StripePaymentGatewayAdapter paymentGatewayAdapter;
    public BookingCommandServiceImpl(
            BookingRepository bookingRepository,
            ExternalExperienceService externalExperienceService,
            StripePaymentGatewayAdapter paymentGatewayAdapter
    ) {
        this.bookingRepository = bookingRepository;
        this.externalExperienceService = externalExperienceService;
        this.paymentGatewayAdapter = paymentGatewayAdapter;
    }

    @Override
    public Long handle(CreateBookingCommand command) {
        if (!externalExperienceService.existsAvailabilityById(command.availabilityId()))
            throw new IllegalArgumentException("Availability with id " + command.availabilityId() + " does not exist.");

        if (!externalExperienceService.hasSufficientStock(command.availabilityId(), command.ticketTypeId(), command.quantity()))
            throw new IllegalStateException("Not enough stock available for the selected ticket type.");

        var unitPrice = externalExperienceService.getPriceForTicketType(command.availabilityId(), command.ticketTypeId());
        var totalPrice = unitPrice.multiply(java.math.BigDecimal.valueOf(command.quantity()));
        externalExperienceService.decrementStock(command.availabilityId(), command.ticketTypeId(), command.quantity());

        var booking = new Booking(
                new UserId(command.userId()),
                new AvailabilityId(command.availabilityId()),
                new Money(totalPrice, "PEN"),
                command.quantity(),
                BookingStatus.PENDING,
                command.bookingDate()
        );
        bookingRepository.save(booking);
        return booking.getId();
    }

    @Override
    public Optional<Booking> handle(CancelBookingCommand command) {
        var bookingOpt = bookingRepository.findById(command.bookingId());
        if(bookingOpt.isEmpty())
            return Optional.empty();
        var booking = bookingOpt.get();
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only bookings in PENDING state can be cancelled.");
        }
        booking.markAsCancelled();
        bookingRepository.save(booking);
        return Optional.of(booking);
    }

    @Override
    public String handle(InitiatePaymentCommand command) {
        var booking = bookingRepository.findById(command.bookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));

        if (booking.getBookingStatus() != BookingStatus.PENDING)
            throw new IllegalStateException("Booking must be in PENDING state to initiate payment.");

        if (booking.getPayment() != null)
            throw new IllegalStateException("Booking already has a payment initiated.");

        long amountInCents = booking.getTotalBookingPrice()
                .getAmount()
                .multiply(BigDecimal.valueOf(100)).longValue();

        var result = paymentGatewayAdapter
                .processTransaction(command.paymentMethod(), amountInCents, booking.getId());

        if (result == null) {
            throw new IllegalStateException("Stripe failed to create a PaymentIntent.");
        }

        return result.clientSecret();
    }


    @Override
    public Long handle(ProcessPaymentCommand command) {
        var booking = bookingRepository.findById(command.bookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.PENDING)
            throw new IllegalStateException("Booking not in PENDING state.");

        if (booking.getPayment() != null)
            throw new IllegalStateException("Booking already has a payment.");

        var payment = new Payment(
                booking.getTotalBookingPrice(),
                command.paymentMethod(),
                command.transactionId(),
                PaymentStatus.SUCCEEDED,
                Instant.now()
        );

        payment.setBooking(booking);
        booking.addPayment(payment);
        booking.markAsSucceeded();

        return bookingRepository.save(booking).getId();
    }

    @Override
    public boolean handle(FailPaymentCommand command) {
        var booking = bookingRepository.findById(command.bookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be failed.");
        }

        booking.markAsFailed();
        bookingRepository.save(booking);

        return true;
    }

    @Override
    public Long handle(ProcessPayoutCommand command) {
        return 0L;
    }

    @Override
    public boolean handle(CompletePayoutCommand command) {
        return false;
    }

    @Override
    public Long handle(InitiateRefundCommand command) {
        var booking = bookingRepository.findById(command.bookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.SUCCEEDED &&
                booking.getBookingStatus() != BookingStatus.CANCELLED) {
            throw new IllegalStateException("Refunds can only be created for SUCCEEDED or CANCELLED bookings.");
        }
        if (booking.getRefund() != null) {
            throw new IllegalStateException("Refund already exists for this booking.");
        }
        var refund = new Refund(
                booking.getTotalBookingPrice(),
                command.refundReason(),
                RefundStatus.PENDING,
                Instant.now()
        );
        booking.addRefund(refund);
        bookingRepository.save(booking);
        return refund.getId();
    }
}
