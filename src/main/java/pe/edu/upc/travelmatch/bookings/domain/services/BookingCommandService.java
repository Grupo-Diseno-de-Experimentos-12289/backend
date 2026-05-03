package pe.edu.upc.travelmatch.bookings.domain.services;

import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.*;

import java.util.Optional;

public interface BookingCommandService {
    Long handle(CreateBookingCommand command);
    Optional<Booking> handle(CancelBookingCommand command);
    String handle(InitiatePaymentCommand command);
    Long handle(ProcessPaymentCommand command);
    boolean handle(FailPaymentCommand command);
    Long handle(ProcessPayoutCommand command);
    boolean handle(CompletePayoutCommand command);
    Long handle(InitiateRefundCommand command);
}
