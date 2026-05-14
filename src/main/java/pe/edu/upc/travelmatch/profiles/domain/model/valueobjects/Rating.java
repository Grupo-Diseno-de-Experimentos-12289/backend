package pe.edu.upc.travelmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** Rating value carrier. */
@Embeddable
public record Rating(int rating) {
  /** Documentation. */
  public Rating {
    if (rating < 1 || rating > 5) {
      throw new IllegalArgumentException("Rating must be between 1 and 5");
    }
  }

  /** Constructs a new Rating. */
  public Rating() {
    this(1);
  }
}
