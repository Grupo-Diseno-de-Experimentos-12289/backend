package pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;

/**
 * Repository for users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Finds a user by email.
   *
   * @param email user email
   * @return optional user
   */
  Optional<User> findByEmail(String email);

  /**
   * Checks whether a user exists by email.
   *
   * @param email user email
   * @return true if the user exists
   */
  boolean existsByEmail(String email);
}
