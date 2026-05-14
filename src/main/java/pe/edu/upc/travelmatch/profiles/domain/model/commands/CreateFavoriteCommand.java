package pe.edu.upc.travelmatch.profiles.domain.model.commands;

import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

/** CreateFavoriteCommand value carrier. */
public record CreateFavoriteCommand(UserId userId, ExperienceId experienceId) {}
