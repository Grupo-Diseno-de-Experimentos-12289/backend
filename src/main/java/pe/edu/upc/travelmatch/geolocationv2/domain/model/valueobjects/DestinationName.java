package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** DestinationName value carrier. */
@Embeddable
public record DestinationName(String name) {
  /** Constructs a new DestinationName. */
  public DestinationName() {
    this(null);
  }

  /** Documentation. */
  public DestinationName {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Destination name cannot be null or blank");
    }
  }
}
