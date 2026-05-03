package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyResource;

public class CreateAgencyCommandFromResourceAssembler {
    public static CreateAgencyCommand toCommandFromResource(CreateAgencyResource resource) {
        return new CreateAgencyCommand(
                resource.name(),
                resource.description(),
                resource.ruc(),
                resource.contactEmail(),
                resource.contactPhone(),
                resource.userId()
        );
    }
}