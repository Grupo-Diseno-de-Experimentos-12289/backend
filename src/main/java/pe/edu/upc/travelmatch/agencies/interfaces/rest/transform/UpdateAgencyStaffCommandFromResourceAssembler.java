package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyStaffResource;

public class UpdateAgencyStaffCommandFromResourceAssembler {
    public static UpdateAgencyStaffCommand toCommandFromResource(UpdateAgencyStaffResource resource) {
        return new UpdateAgencyStaffCommand(
                resource.id(),
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.phone(),
                resource.position()
        );
    }
}