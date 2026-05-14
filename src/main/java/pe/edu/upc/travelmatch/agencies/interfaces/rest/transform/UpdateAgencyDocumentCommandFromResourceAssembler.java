package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyDocumentResource;

/** UpdateAgencyDocumentCommandFromResourceAssembler type. */
public class UpdateAgencyDocumentCommandFromResourceAssembler {
  /** To command from resource. */
  public static UpdateAgencyDocumentCommand toCommandFromResource(
      UpdateAgencyDocumentResource resource) {
    return new UpdateAgencyDocumentCommand(
        resource.id(), resource.documentType(), resource.documentUrl(), resource.description());
  }
}
