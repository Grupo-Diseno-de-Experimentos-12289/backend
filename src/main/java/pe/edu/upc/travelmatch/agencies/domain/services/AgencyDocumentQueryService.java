package pe.edu.upc.travelmatch.agencies.domain.services;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyDocumentsByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyDocumentByIdQuery;

import java.util.List;
import java.util.Optional;

public interface AgencyDocumentQueryService {

    List<AgencyDocument> handle(GetAllAgencyDocumentsByAgencyIdQuery query);

    Optional<AgencyDocument> handle(GetAgencyDocumentByIdQuery query);
}