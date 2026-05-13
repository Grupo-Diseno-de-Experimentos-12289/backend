package pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.valueobjects.Roles;

/**
 * Repository for roles.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  /**
   * Finds a role by name.
   *
   * @param name role name
   * @return optional role
   */
  Optional<Role> findByName(Roles name);

  /**
   * Checks whether a role exists by name.
   *
   * @param name role name
   * @return true if role exists
   */
  boolean existsByName(Roles name);
}
