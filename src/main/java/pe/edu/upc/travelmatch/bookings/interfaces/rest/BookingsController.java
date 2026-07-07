package pe.edu.upc.travelmatch.bookings.interfaces.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.CreateBookingCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.ProcessPaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingByIdQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingsByUserIdQuery;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.TransactionId;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingCommandService;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingQueryService;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.*;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.transform.*;

/** BookingsController type. */
@RestController
@RequestMapping(value = "/api/v1/bookings", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Bookings", description = "Bookings Management Endpoints")
public class BookingsController {

  private final BookingCommandService bookingCommandService;
  private final BookingQueryService bookingQueryService;

  /**
   * Constructor.
   *
   * @param bookingCommandService the booking command service
   * @param bookingQueryService the booking query service
   */
  public BookingsController(
      BookingCommandService bookingCommandService, BookingQueryService bookingQueryService) {
    this.bookingCommandService = bookingCommandService;
    this.bookingQueryService = bookingQueryService;
  }

  /** Create booking. */
  @PostMapping
  public ResponseEntity<BookingResource> createBooking(@RequestBody CreateBookingResource resource) {
    try {
      CreateBookingCommand command =
          CreateBookingCommandFromResourceAssembler.toCommandFromResource(resource);
      Long bookingId = bookingCommandService.handle(command);
      var bookingOpt = bookingQueryService.handle(new GetBookingByIdQuery(bookingId));

      if (bookingOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
      }
      BookingResource bookingResource =
          BookingResourceFromEntityAssembler.toResourceFromEntity(bookingOpt.get());
      return new ResponseEntity<>(bookingResource, HttpStatus.CREATED);
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  /** Cancel booking. */
  @PostMapping("/{bookingId}/cancel")
  public ResponseEntity<BookingResource> cancelBooking(
      @PathVariable Long bookingId, @RequestBody CancelBookingResource resource) {
    try {
      var cancelCommand =
          CancelBookingCommandFromResourceAssembler.toCommandFromResource(bookingId, resource);
      var cancelledBookingOpt = bookingCommandService.handle(cancelCommand);
      if (cancelledBookingOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
      }

      var bookingResource =
          BookingResourceFromEntityAssembler.toResourceFromEntity(cancelledBookingOpt.get());
      return new ResponseEntity<>(bookingResource, HttpStatus.OK);
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  /** Initiate refund. */
  @PostMapping("/{bookingId}/refunds")
  public ResponseEntity<RefundResource> initiateRefund(
      @PathVariable Long bookingId, @RequestBody InitiateRefundResource resource) {
    try {
      var command =
          InitiateRefundCommandFromResourceAssembler.toCommandFromResource(bookingId, resource);
      var refundId = bookingCommandService.handle(command);
      var booking = bookingQueryService.handle(new GetBookingByIdQuery(bookingId));
      if (booking.isEmpty() || booking.get().getRefund() == null) {
        return ResponseEntity.notFound().build();
      }
      var refundResource =
          RefundResourceFromEntityAssembler.toResourceFromEntity(booking.get().getRefund());
      return new ResponseEntity<>(refundResource, HttpStatus.CREATED);
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  /** Initiate payment. */
  @PostMapping("/{bookingId}/payments/initiate")
  public ResponseEntity<String> initiatePayment(
      @PathVariable Long bookingId, @RequestBody InitiatePaymentResource resource) {

    if (!bookingId.equals(resource.bookingId())) {
      return ResponseEntity.badRequest().build();
    }

    var command = InitiatePaymentCommandFromResourceAssembler.toCommandFromResource(resource);
    var clientSecret = bookingCommandService.handle(command);

    return ResponseEntity.ok(clientSecret);
  }

  /** Fail payment. */
  @PostMapping("/fail-payment")
  public ResponseEntity<Void> failPayment(@RequestBody FailPaymentResource resource) {
    var command = FailPaymentCommandFromResourceAssembler.toCommandFromResource(resource);
    var success = bookingCommandService.handle(command);
    return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
  }

  /** Get bookings by user ID. */
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<BookingResource>> getBookingsByUserId(@PathVariable Long userId) {
    var query = new GetBookingsByUserIdQuery(new UserId(userId));
    var bookings = bookingQueryService.handle(query);
    var resources = bookings.stream()
        .map(BookingResourceFromEntityAssembler::toResourceFromEntity)
        .toList();
    return ResponseEntity.ok(resources);
  }

  /** Mock confirm payment. */
  @PostMapping("/{bookingId}/confirm-payment")
  public ResponseEntity<BookingResource> confirmPayment(@PathVariable Long bookingId) {
    var command = new ProcessPaymentCommand(bookingId, "mock_method", new TransactionId("mock_txn_" + bookingId));
    bookingCommandService.handle(command);
    var bookingOpt = bookingQueryService.handle(new GetBookingByIdQuery(bookingId));
    return bookingOpt.map(booking -> ResponseEntity.ok(BookingResourceFromEntityAssembler.toResourceFromEntity(booking)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
