package pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;

/** AgencyDocumentRepository contract. */
@Repository
public interface AgencyDocumentRepository extends JpaRepository<AgencyDocument, Long> {
  /** Find by agency id. */
  List<AgencyDocument> findByAgencyId(Long agencyId);
}
