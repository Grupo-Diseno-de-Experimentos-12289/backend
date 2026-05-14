package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyDocumentResource;

/** CreateAgencyDocumentCommandFromResourceAssembler type. */
public class CreateAgencyDocumentCommandFromResourceAssembler {
  /** To command from resource. */
  public static CreateAgencyDocumentCommand toCommandFromResource(
      Long agencyId, CreateAgencyDocumentResource resource) {
    return new CreateAgencyDocumentCommand(
        agencyId, resource.documentType(), resource.documentUrl(), resource.description());
  }
}
