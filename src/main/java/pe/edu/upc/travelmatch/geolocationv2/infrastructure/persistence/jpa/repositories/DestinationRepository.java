package pe.edu.upc.travelmatch.geolocationv2.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.valueobjects.DestinationName;

import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    boolean existsByName(DestinationName name);
    boolean existsByNameAndIdIsNot(DestinationName name, Long id);
    Optional<Destination> findByName(DestinationName name);
}
