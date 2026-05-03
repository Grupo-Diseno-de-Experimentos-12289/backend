package pe.edu.upc.travelmatch.experiences.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.*;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.*;

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

    public Experience(String title, String description, AgencyId agencyId, Category category, DestinationId destinationId,
                      String duration, String meetingPoint) {
        this.title = title;
        this.description = description;
        this.agencyId = agencyId;
        this.category = category;
        this.destinationId = destinationId;
        this.duration = duration;
        this.meetingPoint = meetingPoint;
    }

    public void markAsDeleted() {
        this.deletedAt = new Date();
    }

    public void updateInfo(String title, String description, Category category, DestinationId destinationId,
                           String duration, String meetingPoint) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.destinationId = destinationId;
        this.duration = duration;
        this.meetingPoint = meetingPoint;
    }
}