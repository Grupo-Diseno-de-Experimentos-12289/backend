package pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
/**
 * Repository interface for managing Availability entities.
 */
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
  /**
   * Finds all non-deleted availabilities.
   *
   * @return list of availabilities
   */
  List<Availability> findAllByDeletedAtIsNull();
}
