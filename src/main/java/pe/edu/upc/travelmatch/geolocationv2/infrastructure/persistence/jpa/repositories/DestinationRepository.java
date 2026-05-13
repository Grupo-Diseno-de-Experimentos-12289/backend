package pe.edu.upc.travelmatch.geolocationv2.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.DestinationName;

/**
 * Destination repository.
 */
@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

  /**
   * Check if a destination exists by name.
   *
   * @param name the destination name
   * @return true if exists
   */
  boolean existsByName(DestinationName name);

  /**
   * Check if a destination exists by name and id is not the specified one.
   *
   * @param name the destination name
   * @param id   the destination id
   * @return true if exists
   */
  boolean existsByNameAndIdIsNot(DestinationName name, Long id);

  /**
   * Find a destination by name.
   *
   * @param name the destination name
   * @return the destination if found
   */
  Optional<Destination> findByName(DestinationName name);
}
