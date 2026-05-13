package pe.edu.upc.travelmatch.geolocationv2.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetAllDestinationsQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByDestinationNameQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByIdQuery;

/**
 * Destination query service.
 */
public interface DestinationQueryService {

  /**
   * Handle get all destinations query.
   *
   * @param query the get all query
   * @return the list of destinations
   */
  List<Destination> handle(GetAllDestinationsQuery query);

  /**
   * Handle get destination by destination name query.
   *
   * @param query the get by name query
   * @return the destination if found
   */
  Optional<Destination> handle(GetDestinationByDestinationNameQuery query);

  /**
   * Handle get destination by id query.
   *
   * @param query the get by id query
   * @return the destination if found
   */
  Optional<Destination> handle(GetDestinationByIdQuery query);
}
