package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Country value object.
 *
 * @param country the country name
 */
@Embeddable
public record Country(String country) {
  /**
   * Default constructor.
   */
  public Country() {
    this(null);
  }

  /**
   * Compact constructor.
   *
   * @throws IllegalArgumentException if country is null or blank
   */
  public Country {
    if (country == null || country.isBlank()) {
      throw new IllegalArgumentException("Country cannot be null or blank");
    }
  }
}
