package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "experience_id"})
})
@NoArgsConstructor
public class Favorite extends AuditableAbstractAggregateRoot<Favorite> {
    @Embedded
    @Getter
    @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Embedded
    @Getter
    @AttributeOverride(name = "experienceId", column = @Column(name = "experience_id", nullable = false))
    private ExperienceId experienceId;

    public Favorite(UserId userId, ExperienceId experienceId) {
        this.userId = userId;
        this.experienceId = experienceId;
    }

    public boolean belongsTo(UserId userId) {
        return this.userId.equals(userId);
    }

}
