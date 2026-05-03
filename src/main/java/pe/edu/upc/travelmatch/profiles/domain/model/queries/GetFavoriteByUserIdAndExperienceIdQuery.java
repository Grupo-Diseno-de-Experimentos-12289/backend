package pe.edu.upc.travelmatch.profiles.domain.model.queries;

import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

public record GetFavoriteByUserIdAndExperienceIdQuery(UserId userId, ExperienceId experienceId) {
}
