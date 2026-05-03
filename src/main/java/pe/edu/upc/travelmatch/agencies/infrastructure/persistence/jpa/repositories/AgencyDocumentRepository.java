package pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;

import java.util.List;

@Repository
public interface AgencyDocumentRepository extends JpaRepository<AgencyDocument, Long> {
    List<AgencyDocument> findByAgencyId(Long agencyId);
}