package pe.edu.upc.travelmatch.agencies.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgenciesQuery;

/** AgencyQueryService contract. */
public interface AgencyQueryService {
  /** Handle. */
  Optional<Agency> handle(GetAgencyByIdQuery query);

  /** Handle. */
  List<Agency> handle(GetAllAgenciesQuery query);
}
