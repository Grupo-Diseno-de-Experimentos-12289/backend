package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateAvailabilityCommand;

/** Service to manage Availability commands. */
public interface AvailabilityCommandService {
  /**
   * Handles the CreateAvailabilityCommand.
   *
   * @param command the command object
   * @return the assigned availability ID
   */
  Long handle(CreateAvailabilityCommand command);

  /**
   * Updates an existing availability.
   *
   * @param command the update command object
   */
  void updateAvailability(UpdateAvailabilityCommand command);

  /**
   * Deletes an existing availability by its ID.
   *
   * @param availabilityId the availability ID
   */
  void deleteAvailability(Long availabilityId);
}
