package pe.edu.upc.travelmatch.experiences.domain.services;
import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAvailabilityByIdQuery;
/**
 * Service to manage Availability queries.
 */
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
}
