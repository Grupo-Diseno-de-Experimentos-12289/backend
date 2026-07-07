package pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** State value carrier. */
@Embeddable
public record State(String state) {
  /** Constructs a new State. */
  public State() {
    this(null);
  }

  /** Documentation. */
  public State {
    if (state == null || state.isBlank()) {
      throw new IllegalArgumentException("State cannot be null or blank");
    }
  }
}
