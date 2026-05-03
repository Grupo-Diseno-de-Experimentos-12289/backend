package pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;

import java.util.Optional;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {
    boolean existsByRuc(String ruc);
    boolean existsByContactEmail(String contactEmail);
    boolean existsByContactEmailAndIdIsNot(String contactEmail, Long currentAgencyId);
    boolean existsByUserId(Long userId);
}