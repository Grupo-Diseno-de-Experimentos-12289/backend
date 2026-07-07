package pe.edu.upc.travelmatch.geolocationv2.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetAllDestinationsQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByDestinationNameQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByIdQuery;

/** DestinationQueryService contract. */
public interface DestinationQueryService {
  /** Handle. */
  List<Destination> handle(GetAllDestinationsQuery query);

  /** Handle. */
  Optional<Destination> handle(GetDestinationByDestinationNameQuery query);

  /** Handle. */
  Optional<Destination> handle(GetDestinationByIdQuery query);
}
