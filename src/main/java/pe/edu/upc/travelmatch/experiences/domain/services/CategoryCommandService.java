package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedCategoriesCommand;

/** Service to manage Category commands. */
public interface CategoryCommandService {
  /**
   * Handles the SeedCategoriesCommand.
   *
   * @param command the command object
   */
  void handle(SeedCategoriesCommand command);
}
