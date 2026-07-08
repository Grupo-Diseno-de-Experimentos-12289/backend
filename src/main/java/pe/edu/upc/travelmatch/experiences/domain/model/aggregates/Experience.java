package pe.edu.upc.travelmatch.experiences.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private CancellationPolicyType cancellationPolicyType;

  @Column(columnDefinition = "TEXT")
  private String cancellationPolicyDescription;

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
   * @param cancellationPolicyType the cancellation policy type
   * @param cancellationPolicyDescription the human-readable cancellation policy details
   */
  public Experience(
      String title,
      String description,
      AgencyId agencyId,
      Category category,
      DestinationId destinationId,
      String duration,
      String meetingPoint,
      CancellationPolicyType cancellationPolicyType,
      String cancellationPolicyDescription) {
    this.title = title;
    this.description = description;
    this.agencyId = agencyId;
    this.category = category;
    this.destinationId = destinationId;
    this.duration = duration;
    this.meetingPoint = meetingPoint;
    this.cancellationPolicyType =
        cancellationPolicyType == null ? CancellationPolicyType.FLEXIBLE : cancellationPolicyType;
    this.cancellationPolicyDescription = cancellationPolicyDescription;
  }

  /** Legacy 7-parameter constructor for compatibility with older code/tests. */
  public Experience(
      String title,
      String description,
      AgencyId agencyId,
      Category category,
      DestinationId destinationId,
      String duration,
      String meetingPoint) {
    this(title, description, agencyId, category, destinationId, duration, meetingPoint, CancellationPolicyType.FLEXIBLE, "Flexible cancellation policy.");
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
   * @param cancellationPolicyType the new cancellation policy type
   * @param cancellationPolicyDescription the new cancellation policy details
   */
  public void updateInfo(
      String title,
      String description,
      Category category,
      DestinationId destinationId,
      String duration,
      String meetingPoint,
      CancellationPolicyType cancellationPolicyType,
      String cancellationPolicyDescription) {
    this.title = title;
    this.description = description;
    this.category = category;
    this.destinationId = destinationId;
    this.duration = duration;
    this.meetingPoint = meetingPoint;
    this.cancellationPolicyType =
        cancellationPolicyType == null ? this.cancellationPolicyType : cancellationPolicyType;
    this.cancellationPolicyDescription = cancellationPolicyDescription;
  }

  /** Legacy 6-parameter updateInfo method for compatibility with older code/tests. */
  public void updateInfo(
      String title,
      String description,
      Category category,
      DestinationId destinationId,
      String duration,
      String meetingPoint) {
    this.updateInfo(title, description, category, destinationId, duration, meetingPoint, this.cancellationPolicyType, this.cancellationPolicyDescription);
  }
}
