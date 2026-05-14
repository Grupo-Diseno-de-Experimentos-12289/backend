package pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;

/** AgencyStaffRepository contract. */
@Repository
public interface AgencyStaffRepository extends JpaRepository<AgencyStaff, Long> {

  /** Find by agency id. */
  List<AgencyStaff> findByAgencyId(Long agencyId);

  /** Exists by email. */
  boolean existsByEmail(String email);

  /** Find by email. */
  Optional<AgencyStaff> findByEmail(String email);
}
