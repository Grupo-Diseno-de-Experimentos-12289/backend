package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyDocumentResource;

public class UpdateAgencyDocumentCommandFromResourceAssembler {
    public static UpdateAgencyDocumentCommand toCommandFromResource(UpdateAgencyDocumentResource resource) {
        return new UpdateAgencyDocumentCommand(
                resource.id(),
                resource.documentType(),
                resource.documentUrl(),
                resource.description()
        );
    }
}