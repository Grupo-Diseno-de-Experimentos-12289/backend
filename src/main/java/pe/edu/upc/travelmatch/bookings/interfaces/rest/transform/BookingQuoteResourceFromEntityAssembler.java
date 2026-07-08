package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.BookingQuote;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.BookingQuoteResource;

/** BookingQuoteResourceFromEntityAssembler type. */
public class BookingQuoteResourceFromEntityAssembler {
    /** To resource from entity. */
    public static BookingQuoteResource toResourceFromEntity(BookingQuote quote) {
        return new BookingQuoteResource(
                quote.availabilityId(),
                quote.ticketTypeId(),
                quote.quantity(),
                quote.unitPrice().getAmount(),
                quote.totalPrice().getAmount(),
                quote.totalPrice().getCurrency(),
                quote.stockAvailable(),
                quote.cancellationPolicy());
    }
}