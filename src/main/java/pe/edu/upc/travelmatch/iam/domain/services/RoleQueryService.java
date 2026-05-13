package pe.edu.upc.travelmatch.iam.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllRolesQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetRoleByIdQuery;

/**
 * Role query service interface.
 */
public interface RoleQueryService {

  /**
   * Handle get all roles query.
   *
   * @param query the get all roles query
   * @return the list of roles
   */
  List<Role> handle(GetAllRolesQuery query);

  /**
   * Handle get role by id query.
   *
   * @param query the get role by id query
   * @return the role if found
   */
  Optional<Role> handle(GetRoleByIdQuery query);
}
