package pe.edu.upc.travelmatch.bookings.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.CreateBookingCommand;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingCommandService;
import pe.edu.upc.travelmatch.bookings.domain.model.queries.GetBookingByIdQuery;
import pe.edu.upc.travelmatch.bookings.domain.services.BookingQueryService;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.*;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.transform.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/bookings", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Bookings", description = "Bookings Management Endpoints")
public class BookingsController {

    private final BookingCommandService bookingCommandService;
    private final BookingQueryService bookingQueryService;

    public BookingsController(BookingCommandService bookingCommandService, BookingQueryService bookingQueryService) {
        this.bookingCommandService = bookingCommandService;
        this.bookingQueryService = bookingQueryService;
    }

    @PostMapping
    public ResponseEntity<BookingResource> createBooking(@RequestBody CreateBookingResource resource) {
        try {
            CreateBookingCommand command = CreateBookingCommandFromResourceAssembler.toCommandFromResource(resource);
            Long bookingId = bookingCommandService.handle(command);
            var bookingOpt = bookingQueryService.handle(new GetBookingByIdQuery(bookingId));
            if (bookingOpt.isEmpty()) return ResponseEntity.notFound().build();
            BookingResource bookingResource = BookingResourceFromEntityAssembler.toResourceFromEntity(bookingOpt.get());
            return new ResponseEntity<>(bookingResource, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResource> cancelBooking(
            @PathVariable Long bookingId,
            @RequestBody CancelBookingResource resource) {
        try {
            var cancelCommand = CancelBookingCommandFromResourceAssembler
                    .toCommandFromResource(bookingId, resource);
            var cancelledBookingOpt = bookingCommandService.handle(cancelCommand);
            if (cancelledBookingOpt.isEmpty()) return ResponseEntity.notFound().build();

            var bookingResource = BookingResourceFromEntityAssembler
                    .toResourceFromEntity(cancelledBookingOpt.get());
            return new ResponseEntity<>(bookingResource, HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{bookingId}/refunds")
    public ResponseEntity<RefundResource> initiateRefund(
            @PathVariable Long bookingId,
            @RequestBody InitiateRefundResource resource
    ) {
        try {
            var command = InitiateRefundCommandFromResourceAssembler.toCommandFromResource(bookingId, resource);
            var refundId = bookingCommandService.handle(command);
            var booking = bookingQueryService.handle(new GetBookingByIdQuery(bookingId));
            if (booking.isEmpty() || booking.get().getRefund() == null) {
                return ResponseEntity.notFound().build();
            }
            var refundResource = RefundResourceFromEntityAssembler.toResourceFromEntity(booking.get().getRefund());
            return new ResponseEntity<>(refundResource, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{bookingId}/payments/initiate")
    public ResponseEntity<String> initiatePayment(
            @PathVariable Long bookingId,
            @RequestBody InitiatePaymentResource resource) {

        if (!bookingId.equals(resource.bookingId())) {
            return ResponseEntity.badRequest().build();
        }

        var command = InitiatePaymentCommandFromResourceAssembler.toCommandFromResource(resource);
        var clientSecret = bookingCommandService.handle(command);

        return ResponseEntity.ok(clientSecret);
    }

    @PostMapping("/fail-payment")
    public ResponseEntity<?> failPayment(@RequestBody FailPaymentResource resource) {
        var command = FailPaymentCommandFromResourceAssembler.toCommandFromResource(resource);
        var success = bookingCommandService.handle(command);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}