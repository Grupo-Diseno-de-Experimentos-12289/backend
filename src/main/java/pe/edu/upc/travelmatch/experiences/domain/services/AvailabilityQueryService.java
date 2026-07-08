package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAvailabilitiesByExperienceIdQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAvailabilityByIdQuery;

import java.util.List;
import java.util.Optional;

/** Service to manage Availability queries. */
public interface AvailabilityQueryService {
  /**
   * Retrieves all non-deleted availabilities.
   *
   * @return the list of availabilities
   */
  List<Availability> getAllAvailabilities();

  /**
   * Handles the get availability by id query.
   *
   * @param query the query object
   * @return the matching availability if found
   */
  Optional<Availability> handle(GetAvailabilityByIdQuery query);

  /**
   * Handles the get availabilities by experience id query.
   *
   * @param query the query object
   * @return the list of availabilities for the experience, ordered by start date
   */
  List<Availability> handle(GetAvailabilitiesByExperienceIdQuery query);
}
