package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.BookingResource;

/** BookingResourceFromEntityAssembler type. */
public class BookingResourceFromEntityAssembler {
  /** To resource from entity. */
  public static BookingResource toResourceFromEntity(Booking entity) {
    return new BookingResource(
        entity.getId(),
        entity.getUserId().userId(),
        entity.getAvailabilityId().availabilityId(),
        entity.getQuantity(),
        entity.getTotalBookingPrice().getCurrency(),
        entity.getTotalBookingPrice().getAmount(),
        entity.getBookingStatus().name(),
        entity.getBookingDate());
  }
}
