package pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;

/** AgencyRepository contract. */
@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {
  /** Exists by ruc. */
  boolean existsByRuc(String ruc);

  /** Exists by contact email. */
  boolean existsByContactEmail(String contactEmail);

  /** Exists by contact email and id is not. */
  boolean existsByContactEmailAndIdIsNot(String contactEmail, Long currentAgencyId);

  /** Exists by user id. */
  boolean existsByUserId(Long userId);
}
