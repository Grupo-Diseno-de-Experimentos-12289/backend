package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/** Documentation. */
@Entity
@Table(
    name = "favorites",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "experience_id"})})
@NoArgsConstructor
public class Favorite extends AuditableAbstractAggregateRoot<Favorite> {
  @Embedded
  @Getter
  @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false))
  private UserId userId;

  @Embedded
  @Getter
  @AttributeOverride(
      name = "experienceId",
      column = @Column(name = "experience_id", nullable = false))
  private ExperienceId experienceId;

  /** Constructs a new Favorite. */
  public Favorite(UserId userId, ExperienceId experienceId) {
    this.userId = userId;
    this.experienceId = experienceId;
  }

  /** Belongs to. */
  public boolean belongsTo(UserId userId) {
    return this.userId.equals(userId);
  }
}
