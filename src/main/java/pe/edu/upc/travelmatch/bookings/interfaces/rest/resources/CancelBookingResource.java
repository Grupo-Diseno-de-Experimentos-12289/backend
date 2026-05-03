package pe.edu.upc.travelmatch.bookings.interfaces.rest.resources;

public record CancelBookingResource(
        Long userId,
        String reason
) { }