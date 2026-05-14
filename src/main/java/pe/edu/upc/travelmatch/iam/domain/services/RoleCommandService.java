package pe.edu.upc.travelmatch.iam.domain.services;

import pe.edu.upc.travelmatch.iam.domain.model.commands.SeedRolesCommand;

/** RoleCommandService contract. */
public interface RoleCommandService {
  /** Handle. */
  void handle(SeedRolesCommand command);
}
