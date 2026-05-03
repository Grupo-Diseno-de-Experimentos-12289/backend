package pe.edu.upc.travelmatch.bookings.application.internal.queryservices;

import org.springframework.stereotype.Service;
//import pe.edu.upc.travelmatch.bookings.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingByIdQuery;
//import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingsByAgencyIdQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingsByUserIdQuery;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingQueryService;
import pe.edu.upc.travelmatch.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookingQueryServiceImpl implements BookingQueryService {

    private final BookingRepository bookingRepository;

    public BookingQueryServiceImpl(
            BookingRepository bookingRepository
    ) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Optional<Booking> handle(GetBookingByIdQuery query) {
        return bookingRepository.findById(query.bookingId());
    }

//    @Override
//    public List<Booking> handle(GetBookingsByAgencyIdQuery query) {
//        return List.of();
//    }

    @Override
    public List<Booking> handle(GetBookingsByUserIdQuery query) {
        return bookingRepository.findAllByUserId(query.userId());
    }
}
