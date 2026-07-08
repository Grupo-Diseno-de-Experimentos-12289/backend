package pe.edu.upc.travelmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** AvailabilityId value carrier. */
@Embeddable
public record AvailabilityId(Long availabilityId) {
  /** Documentation. */
  public AvailabilityId {
    if (availabilityId < 0) {
      throw new IllegalArgumentException("The availability id must be greater than zero");
    }
  }

  /** Constructs a new AvailabilityId. */
  public AvailabilityId() {
    this(0L);
  }
}
