package pe.edu.upc.travelmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** ExperienceId value carrier. */
@Embeddable
public record ExperienceId(Long experienceId) {
  /** Documentation. */
  public ExperienceId {
    if (experienceId < 0) {
      throw new IllegalArgumentException("The experience id must be greater than zero");
    }
  }

  /** Constructs a new ExperienceId. */
  public ExperienceId() {
    this(0L);
  }
}
