package pe.edu.upc.travelmatch.experiences.domain.services;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceCommand;
/**
 * Service to manage Experience commands.
 */
public interface ExperienceCommandService {
  /**
   * Handles the CreateExperienceCommand.
   *
   * @param command the command object
   * @return the created experience ID
   */
  Long handle(CreateExperienceCommand command);
  /**
   * Deletes an existing experience by its ID.
   *
   * @param experienceId the experience ID
   */
  void deleteExperience(Long experienceId);
  /**
   * Handles the UpdateExperienceCommand.
   *
   * @param command the command object
   */
  void updateExperience(UpdateExperienceCommand command);
}
