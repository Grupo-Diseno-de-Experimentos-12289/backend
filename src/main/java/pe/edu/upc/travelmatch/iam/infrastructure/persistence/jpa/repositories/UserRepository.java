package pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;

/** UserRepository contract. */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  /** Find by email. */
  Optional<User> findByEmail(String email);

  /** Exists by email. */
  boolean existsByEmail(String email);
}
