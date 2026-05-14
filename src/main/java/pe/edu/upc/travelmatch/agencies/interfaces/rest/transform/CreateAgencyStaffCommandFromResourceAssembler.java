package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyStaffResource;

/** CreateAgencyStaffCommandFromResourceAssembler type. */
public class CreateAgencyStaffCommandFromResourceAssembler {
  /** To command from resource. */
  public static CreateAgencyStaffCommand toCommandFromResource(
      Long agencyId, CreateAgencyStaffResource resource) {
    return new CreateAgencyStaffCommand(
        agencyId,
        resource.firstName(),
        resource.lastName(),
        resource.email(),
        resource.phone(),
        resource.position());
  }
}
