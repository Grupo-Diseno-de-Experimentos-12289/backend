package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** DestinationAddress value carrier. */
@Embeddable
public record DestinationAddress(String address) {
  /** Constructs a new DestinationAddress. */
  public DestinationAddress() {
    this(null);
  }

  /** Documentation. */
  public DestinationAddress {
    if (address == null || address.isBlank()) {
      throw new IllegalArgumentException("Destination address cannot be null or blank");
    }
  }
}
