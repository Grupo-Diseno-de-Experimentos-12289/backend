package pe.edu.upc.travelmatch.bookings.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record AvailabilityId(Long availabilityId) {
    public AvailabilityId {
        if (availabilityId < 0) {
            throw new IllegalArgumentException("Availability availabilityId cannot be negative");
        }
    }
    public AvailabilityId() { this(0L); }
}
