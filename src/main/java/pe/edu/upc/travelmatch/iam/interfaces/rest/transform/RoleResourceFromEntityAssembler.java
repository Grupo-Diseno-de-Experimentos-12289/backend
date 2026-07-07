package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.RoleResource;

/** RoleResourceFromEntityAssembler type. */
public class RoleResourceFromEntityAssembler {
  /** To resource from entity. */
  public static RoleResource toResourceFromEntity(Role entity) {
    return new RoleResource(entity.getId(), entity.getStringName());
  }
}
