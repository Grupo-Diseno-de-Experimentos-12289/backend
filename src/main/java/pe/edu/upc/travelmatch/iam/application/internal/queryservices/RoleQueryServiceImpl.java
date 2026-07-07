package pe.edu.upc.travelmatch.iam.application.internal.queryservices;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllRolesQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetRoleByIdQuery;
import pe.edu.upc.travelmatch.iam.domain.services.RoleQueryService;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

/** RoleQueryServiceImpl type. */
@Service
public class RoleQueryServiceImpl implements RoleQueryService {
  private final RoleRepository roleRepository;

  /** Constructs a new RoleQueryServiceImpl. */
  public RoleQueryServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public List<Role> handle(GetAllRolesQuery query) {
    return roleRepository.findAll();
  }

  @Override
  public Optional<Role> handle(GetRoleByIdQuery query) {
    return roleRepository.findById(query.roleId());
  }
}
