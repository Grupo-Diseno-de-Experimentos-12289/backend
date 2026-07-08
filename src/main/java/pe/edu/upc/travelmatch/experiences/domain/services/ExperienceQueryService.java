package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllExperiencesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetExperienceByIdQuery;

import java.util.List;
import java.util.Optional;

/** Service to manage Experience queries. */
public interface ExperienceQueryService {
  /**
   * Handles the GetAllExperiencesQuery.
   *
   * @param query the command object
   * @return list of experiences
   */
  List<Experience> handle(GetAllExperiencesQuery query);

  /**
   * Handles the GetExperienceByIdQuery.
   *
   * @param query the query object
   * @return the optional experience
   */
  Optional<Experience> handle(GetExperienceByIdQuery query);
}
