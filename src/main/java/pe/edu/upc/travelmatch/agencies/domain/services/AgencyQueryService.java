package pe.edu.upc.travelmatch.agencies.domain.services;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgenciesQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyByIdQuery;

import java.util.List;
import java.util.Optional;

public interface AgencyQueryService {
    Optional<Agency> handle(GetAgencyByIdQuery query);
    List<Agency> handle(GetAllAgenciesQuery query);
}