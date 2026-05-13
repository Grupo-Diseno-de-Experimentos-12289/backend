package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * State value object.
 *
 * @param state the state name
 */
@Embeddable
public record State(String state) {
  /**
   * Default constructor.
   */
  public State() {
    this(null);
  }

  /**
   * Compact constructor.
   *
   * @throws IllegalArgumentException if state is null or blank
   */
  public State {
    if (state == null || state.isBlank()) {
      throw new IllegalArgumentException("State cannot be null or blank");
    }
  }
}
