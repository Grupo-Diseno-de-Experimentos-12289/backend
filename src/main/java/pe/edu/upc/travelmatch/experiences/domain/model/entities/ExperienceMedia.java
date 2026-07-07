package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

/** Entity representing the media attached to an experience. */
@Getter
@Entity
@Table(name = "experience_media")
@NoArgsConstructor
public class ExperienceMedia extends AuditableModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "experience_id", nullable = false)
  private Experience experience;

  @Column(name = "media_url", nullable = false, length = 256)
  private String mediaUrl;

  @Column(name = "caption", length = 256)
  private String caption;

  @Column(name = "deleted_at")
  private Date deletedAt;

  /**
   * Constructor for ExperienceMedia.
   *
   * @param experience the associated experience
   * @param mediaUrl the media URL
   * @param caption the caption text
   */
  public ExperienceMedia(Experience experience, String mediaUrl, String caption) {
    this.experience = experience;
    this.mediaUrl = mediaUrl;
    this.caption = caption;
  }

  /**
   * Updates the media details.
   *
   * @param mediaUrl the new media URL
   * @param caption the new caption text
   */
  public void update(String mediaUrl, String caption) {
    this.mediaUrl = mediaUrl;
    this.caption = caption;
  }

  /** Marks this media as logically deleted. */
  public void markAsDeleted() {
    this.deletedAt = new Date();
  }
}
