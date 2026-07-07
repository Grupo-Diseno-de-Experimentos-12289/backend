package pe.edu.upc.travelmatch.agencies.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyDocumentByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyDocumentsByAgencyIdQuery;

/** AgencyDocumentQueryService contract. */
public interface AgencyDocumentQueryService {

  /** Handle. */
  List<AgencyDocument> handle(GetAllAgencyDocumentsByAgencyIdQuery query);

  /** Handle. */
  Optional<AgencyDocument> handle(GetAgencyDocumentByIdQuery query);
}
