package pe.edu.upc.travelmatch.iam.domain.services;

import pe.edu.upc.travelmatch.iam.domain.model.commands.SeedRolesCommand;

/**
 * Role command service interface.
 */
public interface RoleCommandService {

  /**
   * Handle seed roles command.
   *
   * @param command the seed roles command
   */
  void handle(SeedRolesCommand command);
}
