package pe.edu.upc.travelmatch.geolocationv2.domain.services;

import pe.edu.upc.travelmatch.geolocationv2.domain.model.aggregates.Destination;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetAllDestinationsQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByDestinationNameQuery;
import pe.edu.upc.travelmatch.geolocationv2.domain.model.queries.GetDestinationByIdQuery;

import java.util.List;
import java.util.Optional;

public interface DestinationQueryService {
    List<Destination> handle(GetAllDestinationsQuery query);
    Optional<Destination> handle(GetDestinationByDestinationNameQuery query);
    Optional<Destination> handle(GetDestinationByIdQuery query);
}
