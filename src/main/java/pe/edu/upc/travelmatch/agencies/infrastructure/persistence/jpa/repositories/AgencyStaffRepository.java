package pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgencyStaffRepository extends JpaRepository<AgencyStaff, Long> {

    List<AgencyStaff> findByAgencyId(Long agencyId);

    boolean existsByEmail(String email);

    Optional<AgencyStaff> findByEmail(String email);
}