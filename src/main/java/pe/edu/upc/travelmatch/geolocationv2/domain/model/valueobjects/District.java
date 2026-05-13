package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** District value carrier. */
@Embeddable
public record District(String district) {
  /** Constructs a new District. */
  public District() {
    this(null);
  }

  /** Documentation. */
  public District {
    if (district == null || district.isBlank()) {
      throw new IllegalArgumentException("District cannot be null or blank");
    }
  }
}
