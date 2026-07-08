package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllExperiencesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetExperienceByIdQuery;

import java.util.List;
import java.util.Optional;

/** Service to manage ExperienceMedia queries. */
public interface ExperienceMediaQueryService {
  /**
   * Retrieves all experience media items.
   *
   * @return list of experience media
   */
  List<ExperienceMedia> getAll();

  /**
   * Returns media list matching the specific experience identifier.
   *
   * @param experienceId the experience ID
   * @return list of experience media
   */
  List<ExperienceMedia> findByExperienceId(Long experienceId);

  /**
   * Retrieves specific media given its unique identifier.
   *
   * @param id the unique media ID
   * @return optional containing experience media if found
   */
  Optional<ExperienceMedia> findById(Long id);

  /**
   * Retrieves all experiences wrapper for media.
   *
   * @param query the query
   * @return the list of media items
   */
  List<ExperienceMedia> handle(GetAllExperiencesQuery query);

  /**
   * Retrieves specific media given an experience id query.
   *
   * @param query the query containing the id
   * @return optional containing experience media
   */
  Optional<ExperienceMedia> handle(GetExperienceByIdQuery query);
}
