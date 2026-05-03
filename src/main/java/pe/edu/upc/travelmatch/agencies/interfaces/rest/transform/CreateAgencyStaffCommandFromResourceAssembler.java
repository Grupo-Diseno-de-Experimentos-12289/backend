package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyStaffResource;

public class CreateAgencyStaffCommandFromResourceAssembler {
    public static CreateAgencyStaffCommand toCommandFromResource(Long agencyId, CreateAgencyStaffResource resource) {
        return new CreateAgencyStaffCommand(
                agencyId,
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.phone(),
                resource.position()
        );
    }
}