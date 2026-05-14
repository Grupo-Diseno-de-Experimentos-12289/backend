package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyDocumentResource;

/** AgencyDocumentResourceFromEntityAssembler type. */
public class AgencyDocumentResourceFromEntityAssembler {
  /** To resource from entity. */
  public static AgencyDocumentResource toResourceFromEntity(AgencyDocument entity) {
    return new AgencyDocumentResource(
        entity.getId(),
        entity.getAgency().getId(),
        entity.getDocumentType(),
        entity.getDocumentUrl(),
        entity.getDescription());
  }
}
