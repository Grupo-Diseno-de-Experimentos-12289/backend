package pe.edu.upc.travelmatch.experiences.domain.model.aggregates;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/** Represents an Experience entity. */
@Entity
@Getter
@NoArgsConstructor
public class Experience extends AuditableAbstractAggregateRoot<Experience> {

  @NotBlank
  @Column(nullable = false)
  @Size(min = 5, max = 100)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "agency_id", nullable = false))
  private AgencyId agencyId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "destination_id", nullable = false))
  private DestinationId destinationId;

  private String duration;

  private String meetingPoint;

  private Date deletedAt;

  @OneToMany(mappedBy = "experience", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Availability> availabilities = new ArrayList<>();

  @OneToMany(mappedBy = "experience", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ExperienceMedia> media = new ArrayList<>();

  /**
   * Constructs a new Experience.
   *
   * @param title the experience title
   * @param description the experience description
   * @param agencyId the agency ID
   * @param category the category
   * @param destinationId the destination ID
   * @param duration the typical duration
   * @param meetingPoint the meeting point for the experience
   */
  public Experience(
      String title,
      String description,
      AgencyId agencyId,
      Category category,
      DestinationId destinationId,
      String duration,
      String meetingPoint) {
    this.title = title;
    this.description = description;
    this.agencyId = agencyId;
    this.category = category;
    this.destinationId = destinationId;
    this.duration = duration;
    this.meetingPoint = meetingPoint;
  }

  /** Marks the experience as logically deleted. */
  public void markAsDeleted() {
    this.deletedAt = new Date();
  }

  /**
   * Updates the mutable information of the experience.
   *
   * @param title the new title
   * @param description the new description
   * @param category the new category
   * @param destinationId the new destination ID
   * @param duration the new duration
   * @param meetingPoint the new meeting point
   */
  public void updateInfo(
      String title,
      String description,
      Category category,
      DestinationId destinationId,
      String duration,
      String meetingPoint) {
    this.title = title;
    this.description = description;
    this.category = category;
    this.destinationId = destinationId;
    this.duration = duration;
    this.meetingPoint = meetingPoint;
  }
}
