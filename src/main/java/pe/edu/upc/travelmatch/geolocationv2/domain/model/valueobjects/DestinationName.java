package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Destination name value object.
 *
 * @param name the destination name
 */
@Embeddable
public record DestinationName(String name) {
  /**
   * Default constructor.
   */
  public DestinationName() {
    this(null);
  }

  /**
   * Compact constructor.
   *
   * @throws IllegalArgumentException if name is null or blank
   */
  public DestinationName {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Destination name cannot be null or blank");
    }
  }
}
