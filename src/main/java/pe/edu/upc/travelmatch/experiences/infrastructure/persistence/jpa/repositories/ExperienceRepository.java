package pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;

/** Repository interface for managing Experience entities. */
@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
  /**
   * Validates experiences lacking deletion date logic.
   *
   * @return the list
   */
  List<Experience> findAllByDeletedAtIsNull();
}
