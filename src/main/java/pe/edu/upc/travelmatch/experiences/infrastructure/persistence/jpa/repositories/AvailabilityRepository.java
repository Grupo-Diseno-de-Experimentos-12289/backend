package pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;

import java.util.List;

/** Repository interface for managing Availability entities. */
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
  /**
   * Finds all non-deleted availabilities.
   *
   * @return list of availabilities
   */
  List<Availability> findAllByDeletedAtIsNull();

  /**
   * Finds all non-deleted availabilities by experience ID.
   *
   * @param experienceId the experience ID
   * @return list of availabilities
   */
  List<Availability> findByExperienceIdAndDeletedAtIsNull(Long experienceId);

  /**
   * Finds all non-deleted availabilities for a given experience, ordered by start date.
   *
   * @param experienceId the experience ID
   * @return list of availabilities belonging to the experience
   */
  List<Availability> findAllByExperience_IdAndDeletedAtIsNullOrderByStartDateTimeAsc(
      Long experienceId);
}
