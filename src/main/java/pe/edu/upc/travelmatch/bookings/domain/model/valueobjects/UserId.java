package pe.edu.upc.travelmatch.bookings.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** UserId value carrier. */
@Embeddable
public record UserId(Long userId) {
  /** Documentation. */
  public UserId {
    if (userId < 0) {
      throw new IllegalArgumentException("User userId cannot be negative");
    }
  }

  /** Constructs a new UserId. */
  public UserId() {
    this(0L);
  }
}
