package pe.edu.upc.travelmatch.experiences.domain.services;
import java.util.Optional;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
/**
 * Service to manage ExperienceMedia commands.
 */
public interface ExperienceMediaCommandService {
  /**
   * Handles the CreateExperienceMediaCommand.
   *
   * @param command the command object
   * @return the assigned media ID
   */
  Long handle(CreateExperienceMediaCommand command);
  /**
   * Handles the UpdateExperienceMediaCommand.
   *
   * @param command the command object
   * @return the updated media if successful
   */
  Optional<ExperienceMedia> handle(UpdateExperienceMediaCommand command);
  /**
   * Deletes media by its ID.
   *
   * @param id the media ID
   */
  void deleteById(Long id);
}
