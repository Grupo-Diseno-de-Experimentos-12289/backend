package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyStaffResource;

/** AgencyStaffResourceFromEntityAssembler type. */
public class AgencyStaffResourceFromEntityAssembler {
  /** To resource from entity. */
  public static AgencyStaffResource toResourceFromEntity(AgencyStaff entity) {
    return new AgencyStaffResource(
        entity.getId(),
        entity.getAgency().getId(),
        entity.getFirstName(),
        entity.getLastName(),
        entity.getEmail(),
        entity.getPhone(),
        entity.getPosition());
  }
}
