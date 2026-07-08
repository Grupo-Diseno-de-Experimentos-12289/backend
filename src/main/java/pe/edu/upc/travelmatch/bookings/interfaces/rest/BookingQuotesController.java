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

/** REST controller for booking quote endpoints. */
@RestController
@RequestMapping(value = "/api/v1/bookings/quote")
@Tag(name = "Bookings", description = "Bookings Management Endpoints")
public class BookingQuotesController {

  private final BookingQuoteService bookingQuoteService;

  /**
   * Constructs a new BookingQuotesController.
   *
   * @param bookingQuoteService the service to handle booking quotes
   */
  public BookingQuotesController(BookingQuoteService bookingQuoteService) {
    this.bookingQuoteService = bookingQuoteService;
  }

  /**
   * Returns a real-time booking quote for a given availability, ticket type and quantity.
   *
   * @param availabilityId the availability identifier
   * @param ticketTypeId the ticket type identifier
   * @param quantity the number of tickets requested
   * @return the booking quote resource or 400 if arguments are invalid
   */
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