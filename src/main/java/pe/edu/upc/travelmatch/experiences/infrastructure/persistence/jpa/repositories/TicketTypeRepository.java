package pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;

import java.util.Optional;

/** Repository interface for managing TicketType entities. */
@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
  /**
   * Finds a TicketType by its name.
   *
   * @param name the ticket type enum
   * @return the optional ticket type
   */
  Optional<TicketType> findByName(TicketTypes name);

  /**
   * Checks if a ticket type exists by its name.
   *
   * @param name the ticket type enum
   * @return true if exists, false otherwise
   */
  boolean existsByName(TicketTypes name);
}
