package pe.edu.upc.travelmatch.experiences.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing a Destination ID.
 *
 * @param value the ID value
 */
@Embeddable
public record DestinationId(Long value) {
  /**
   * Constructs a DestinationId.
   *
   * @param value the ID value
   */
  public DestinationId {
    if (value == null || value < 0) {
      throw new IllegalArgumentException("DestinationId must be a positive number");
    }
  }

  /** Default constructor. */
  public DestinationId() {
    this(0L);
  }
}
