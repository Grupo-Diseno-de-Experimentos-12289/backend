package pe.edu.upc.travelmatch.iam.application.internal.queryservices;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllRolesQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetRoleByIdQuery;
import pe.edu.upc.travelmatch.iam.domain.services.RoleQueryService;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

/**
 * Query service that provides read operations related to roles.
 */
@Service
public final class RoleQueryServiceImpl implements RoleQueryService {
  private final RoleRepository roleRepository;

  /**
   * Constructor.
   *
   * @param roleRepositoryDependency repository for roles
   */
  public RoleQueryServiceImpl(final RoleRepository roleRepositoryDependency) {
    this.roleRepository = roleRepositoryDependency;
  }

  /**
   * Returns all roles.
   *
   * @param query query object
   * @return list of roles
   */
  @Override
  public List<Role> handle(final GetAllRolesQuery query) {
    return roleRepository.findAll();
  }

  /**
   * Returns a role by id.
   *
   * @param query query object
   * @return optional role
   */
  @Override
  public Optional<Role> handle(final GetRoleByIdQuery query) {
    return roleRepository.findById(query.roleId());
  }
}
