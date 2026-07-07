package pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;

/** Repository interface for managing Experience Media entities. */
@Repository
public interface ExperienceMediaRepository extends JpaRepository<ExperienceMedia, Long> {
  /**
   * Retrieves an entire chunk of multi-media corresponding to given experience.
   *
   * @param experienceId the matching parent aggregate
   * @return chunk list
   */
  List<ExperienceMedia> findByExperienceId(Long experienceId);
}
