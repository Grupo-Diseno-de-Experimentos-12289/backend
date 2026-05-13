package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.RoleResource;

/**
 * Converts role entities into role resources.
 */
public final class RoleResourceFromEntityAssembler {
  private RoleResourceFromEntityAssembler() {
  }

  /**
   * Converts the given role entity into a resource.
   *
   * @param entity role entity
   * @return role resource
   */
  public static RoleResource toResourceFromEntity(final Role entity) {
    return new RoleResource(entity.getId(), entity.getStringName());
  }
}
