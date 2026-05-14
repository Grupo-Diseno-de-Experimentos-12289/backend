package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Rating;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/** Review type. */
@Entity
@NoArgsConstructor
public class Review extends AuditableAbstractAggregateRoot<Review> {
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

  @Embedded
  @Getter
  @AttributeOverride(name = "rating", column = @Column(name = "rating", nullable = false))
  private Rating rating;

  @Getter
  @Column(length = 2000)
  private String comment;

  /** Constructs a new Review. */
  public Review(UserId userId, ExperienceId experienceId, Rating rating, String comment) {
    this.userId = userId;
    this.experienceId = experienceId;
    this.rating = rating;
    this.comment = comment;
  }

  /** Update rating. */
  public void updateRating(Rating newRating) {
    if (newRating == null) {
      throw new IllegalArgumentException("Rating cannot be null");
    }
    this.rating = newRating;
  }

  /** Update comment. */
  public void updateComment(String newComment) {
    if (newComment == null || newComment.isBlank()) {
      throw new IllegalArgumentException("Comment cannot be null or empty");
    }
    this.comment = newComment;
  }
}
