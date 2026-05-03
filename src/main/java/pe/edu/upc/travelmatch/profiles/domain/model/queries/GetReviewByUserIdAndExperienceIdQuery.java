package pe.edu.upc.travelmatch.profiles.domain.model.queries;

import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

public record GetReviewByUserIdAndExperienceIdQuery(UserId userId, ExperienceId experienceId) {
}
