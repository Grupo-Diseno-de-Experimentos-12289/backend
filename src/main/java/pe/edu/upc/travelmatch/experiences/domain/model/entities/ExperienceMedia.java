package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

import java.util.Date;

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

    public ExperienceMedia(Experience experience, String mediaUrl, String caption) {
        this.experience = experience;
        this.mediaUrl = mediaUrl;
        this.caption = caption;
    }

    public void update(String mediaUrl, String caption) {
        this.mediaUrl = mediaUrl;
        this.caption = caption;
    }

    public void markAsDeleted() {
        this.deletedAt = new Date();
    }

}