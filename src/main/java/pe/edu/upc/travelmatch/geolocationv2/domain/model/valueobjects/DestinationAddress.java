package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Destination address value object.
 *
 * @param address the destination address
 */
@Embeddable
public record DestinationAddress(String address) {
  /**
   * Default constructor.
   */
  public DestinationAddress() {
    this(null);
  }

  /**
   * Compact constructor.
   *
   * @throws IllegalArgumentException if address is null or blank
   */
  public DestinationAddress {
    if (address == null || address.isBlank()) {
      throw new IllegalArgumentException("Destination address cannot be null or blank");
    }
  }
}
