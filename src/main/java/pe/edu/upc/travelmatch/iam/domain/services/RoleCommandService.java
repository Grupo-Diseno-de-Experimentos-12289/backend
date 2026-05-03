package pe.edu.upc.travelmatch.iam.domain.services;

import pe.edu.upc.travelmatch.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
