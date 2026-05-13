package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * District value object.
 *
 * @param district the district name
 */
@Embeddable
public record District(String district) {
  /**
   * Default constructor.
   */
  public District() {
    this(null);
  }

  /**
   * Compact constructor.
   *
   * @throws IllegalArgumentException if district is null or blank
   */
  public District {
    if (district == null || district.isBlank()) {
      throw new IllegalArgumentException("District cannot be null or blank");
    }
  }
}
