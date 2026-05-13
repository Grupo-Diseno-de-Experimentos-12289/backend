package pe.edu.upc.travelmatch.bookings.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingByIdQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingsByUserIdQuery;

/** BookingQueryService contract. */
public interface BookingQueryService {
  /** Handle. */
  Optional<Booking> handle(GetBookingByIdQuery query);

  /** Handle. */
  List<Booking> handle(GetBookingsByUserIdQuery query);
}
