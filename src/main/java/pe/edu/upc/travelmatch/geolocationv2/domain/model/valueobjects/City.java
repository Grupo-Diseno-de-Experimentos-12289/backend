package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * City value object.
 *
 * @param city the city name
 */
@Embeddable
public record City(String city) {
  /**
   * Default constructor.
   */
  public City() {
    this(null);
  }

  /**
   * Compact constructor.
   *
   * @throws IllegalArgumentException if city is null or blank
   */
  public City {
    if (city == null || city.isBlank()) {
      throw new IllegalArgumentException("City cannot be null or blank");
    }
  }
}
