package pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;

import java.util.List;

/** Repository interface for managing Experience entities. */
@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
  /**
   * Validates experiences lacking deletion date logic.
   *
   * @return the list
   */
  List<Experience> findAllByDeletedAtIsNull();

  /**
   * Finds all non-deleted experiences at a given destination whose category matches one of the
   * given interests. Used to power location + interest based recommendations.
   *
   * @param destinationId the destination ID value
   * @param categories the categories (interests) to match
   * @return the matching experiences
   */
  List<Experience> findAllByDestinationId_ValueAndCategory_NameInAndDeletedAtIsNull(
      Long destinationId, List<Categories> categories);
}
