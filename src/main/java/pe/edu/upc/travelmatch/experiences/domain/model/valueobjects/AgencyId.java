package pe.edu.upc.travelmatch.experiences.domain.model.valueobjects;
import jakarta.persistence.Embeddable;
/**
 * Value object representing an Agency ID.
 *
 * @param value the ID value
 */
@Embeddable
public record AgencyId(Long value) {
  /**
   * Constructs an AgencyId.
   *
   * @param value the ID value
   */
  public AgencyId {
    if (value == null || value < 0) {
      throw new IllegalArgumentException("AgencyId must be a positive number");
    }
  }
  /**
   * Default constructor.
   */
  public AgencyId() {
    this(0L);
  }
}
