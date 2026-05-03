package pe.edu.upc.travelmatch.bookings.domain.services;

import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface BookingQueryService {
    Optional<Booking> handle(GetBookingByIdQuery query);
//    List<Booking> handle(GetBookingsByAgencyIdQuery query);
    List<Booking> handle(GetBookingsByUserIdQuery query);
}
