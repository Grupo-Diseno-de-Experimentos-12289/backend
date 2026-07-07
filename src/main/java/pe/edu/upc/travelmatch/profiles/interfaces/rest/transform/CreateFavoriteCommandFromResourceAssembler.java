package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateFavoriteCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateFavoriteResource;

/** CreateFavoriteCommandFromResourceAssembler type. */
public class CreateFavoriteCommandFromResourceAssembler {
  /** To command from resource. */
  public static CreateFavoriteCommand toCommandFromResource(CreateFavoriteResource resource) {
    UserId userId = new UserId(resource.userId());
    ExperienceId experienceId = new ExperienceId(resource.experienceId());
    return new CreateFavoriteCommand(userId, experienceId);
  }
}
