package pe.edu.upc.travelmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** UserId value carrier. */
@Embeddable
public record UserId(Long userId) {
  /** Documentation. */
  public UserId {
    if (userId < 0) {
      throw new IllegalArgumentException("The user id must be greater than zero");
    }
  }

  /** Constructs a new UserId. */
  public UserId() {
    this(0L);
  }
}
