package pe.edu.upc.travelmatch.bookings.domain.services;

import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingQuoteQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingQuote;

/** Service to manage booking quotes. */
public interface BookingQuoteService {
    /**
     * Handles the GetBookingQuoteQuery.
     *
     * @param query the query object
     * @return the booking quote
     */
    BookingQuote handle(GetBookingQuoteQuery query);
}