package pe.edu.upc.travelmatch.iam.application.internal.commandservices;

import java.util.Arrays;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SeedRolesCommand;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.travelmatch.iam.domain.services.RoleCommandService;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

/**
 * Command service that handles role-related operations.
 */
@Service
public class RoleCommandServiceImpl implements RoleCommandService {
  private final RoleRepository roleRepository;

  /**
   * Constructor.
   *
   * @param roleRepository repository for roles
   */
  public RoleCommandServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  /**
   * Seeds all roles defined in {@link Roles} if they do not already exist.
   *
   * @param command the seed roles command
   */
  @Override
  public void handle(SeedRolesCommand command) {
    Arrays.stream(Roles.values()).forEach(role -> {
      if (!roleRepository.existsByName(role)) {
        roleRepository.save(new Role(Roles.valueOf(role.name())));
      }
    });
  }
}
