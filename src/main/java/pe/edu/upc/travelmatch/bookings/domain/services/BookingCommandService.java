package pe.edu.upc.travelmatch.bookings.domain.services;

import java.util.Optional;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.CancelBookingCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.CompletePayoutCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.CreateBookingCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.FailPaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.InitiatePaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.InitiateRefundCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.ProcessPaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.ProcessPayoutCommand;

/** BookingCommandService contract. */
public interface BookingCommandService {
  /** Handle. */
  Long handle(CreateBookingCommand command);

  /** Handle. */
  Optional<Booking> handle(CancelBookingCommand command);

  /** Handle. */
  String handle(InitiatePaymentCommand command);

  /** Handle. */
  Long handle(ProcessPaymentCommand command);

  /** Handle. */
  boolean handle(FailPaymentCommand command);

  /** Handle. */
  Long handle(ProcessPayoutCommand command);

  /** Handle. */
  boolean handle(CompletePayoutCommand command);

  /** Handle. */
  Long handle(InitiateRefundCommand command);
}
