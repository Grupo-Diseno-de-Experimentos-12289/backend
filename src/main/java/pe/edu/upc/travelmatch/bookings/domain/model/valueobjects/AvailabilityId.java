package pe.edu.upc.travelmatch.bookings.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** AvailabilityId value carrier. */
@Embeddable
public record AvailabilityId(Long availabilityId) {
  /** Documentation. */
  public AvailabilityId {
    if (availabilityId < 0) {
      throw new IllegalArgumentException("Availability availabilityId cannot be negative");
    }
  }

  /** Constructs a new AvailabilityId. */
  public AvailabilityId() {
    this(0L);
  }
}
