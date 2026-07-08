package pe.edu.upc.travelmatch.bookings.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/** AgencyId value carrier. */
@Embeddable
public record AgencyId(Long agencyId) {
  /** Documentation. */
  public AgencyId {
    if (agencyId < 0) {
      throw new IllegalArgumentException("Agency agencyId cannot be negative");
    }
  }

  /** Constructs a new AgencyId. */
  public AgencyId() {
    this(0L);
  }
}
