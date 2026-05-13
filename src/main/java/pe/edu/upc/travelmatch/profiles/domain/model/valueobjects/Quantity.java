package pe.edu.upc.travelmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** Quantity value carrier. */
@Embeddable
public record Quantity(int value) {
  /** Documentation. */
  public Quantity {
    if (value < 0) {
      throw new IllegalArgumentException("The quantity must be greater than zero");
    }
  }

  /** Constructs a new Quantity. */
  public Quantity() {
    this(0);
  }
}
