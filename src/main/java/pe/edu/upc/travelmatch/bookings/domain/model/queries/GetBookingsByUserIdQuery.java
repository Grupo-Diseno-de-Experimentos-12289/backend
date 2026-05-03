package pe.edu.upc.travelmatch.bookings.domain.model.queries;

import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.UserId;

public record GetBookingsByUserIdQuery(
        UserId userId
) {
    public GetBookingsByUserIdQuery {
        if (userId == null || userId.userId() == null || userId.userId() <= 0) {
            throw new IllegalArgumentException("UserId userId must not be null or less than or equal to zero.");
        }
    }
}
