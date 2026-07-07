package pe.edu.upc.travelmatch.iam.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllRolesQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetRoleByIdQuery;

/** RoleQueryService contract. */
public interface RoleQueryService {
  /** Handle. */
  List<Role> handle(GetAllRolesQuery query);

  /** Handle. */
  Optional<Role> handle(GetRoleByIdQuery query);
}
