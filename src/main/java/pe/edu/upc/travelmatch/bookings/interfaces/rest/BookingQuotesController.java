package pe.edu.upc.travelmatch.bookings.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingQuoteQuery;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingQuoteService;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.BookingQuoteResource;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.transform.BookingQuoteResourceFromEntityAssembler;

/** BookingQuotesController type. */
@RestController
@RequestMapping(value = "/api/v1/bookings/quote")
@Tag(name = "Bookings", description = "Bookings Management Endpoints")
public class BookingQuotesController {

    private final BookingQuoteService bookingQuoteService;

    /** Constructs a new BookingQuotesController. */
    public BookingQuotesController(BookingQuoteService bookingQuoteService) {
        this.bookingQuoteService = bookingQuoteService;
    }

    /** Get booking quote. */
    @GetMapping
    public ResponseEntity<BookingQuoteResource> getBookingQuote(
            @RequestParam Long availabilityId,
            @RequestParam Long ticketTypeId,
            @RequestParam int quantity) {
        try {
            var query = new GetBookingQuoteQuery(availabilityId, ticketTypeId, quantity);
            var quote = bookingQuoteService.handle(query);
            var resource = BookingQuoteResourceFromEntityAssembler.toResourceFromEntity(quote);
            return ResponseEntity.ok(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}