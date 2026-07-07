package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** City value carrier. */
@Embeddable
public record City(String city) {
  /** Constructs a new City. */
  public City() {
    this(null);
  }

  /** Documentation. */
  public City {
    if (city == null || city.isBlank()) {
      throw new IllegalArgumentException("City cannot be null or blank");
    }
  }
}
