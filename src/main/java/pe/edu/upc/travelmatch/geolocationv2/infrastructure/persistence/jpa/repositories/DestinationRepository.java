package pe.edu.upc.travelmatch.geolocationv2.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.DestinationName;

/** DestinationRepository contract. */
@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
  /** Exists by name. */
  boolean existsByName(DestinationName name);

  /** Exists by name and id is not. */
  boolean existsByNameAndIdIsNot(DestinationName name, Long id);

  /** Find by name. */
  Optional<Destination> findByName(DestinationName name);
}
