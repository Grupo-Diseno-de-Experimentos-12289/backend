package pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.domain.model.valueobjects.Roles;

/** RoleRepository contract. */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  /** Find by name. */
  Optional<Role> findByName(Roles name);

  /** Exists by name. */
  boolean existsByName(Roles name);
}
