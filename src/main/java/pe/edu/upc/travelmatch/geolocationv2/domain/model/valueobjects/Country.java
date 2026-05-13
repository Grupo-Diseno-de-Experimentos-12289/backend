package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** Country value carrier. */
@Embeddable
public record Country(String country) {
  /** Constructs a new Country. */
  public Country() {
    this(null);
  }

  /** Documentation. */
  public Country {
    if (country == null || country.isBlank()) {
      throw new IllegalArgumentException("Country cannot be null or blank");
    }
  }
}
