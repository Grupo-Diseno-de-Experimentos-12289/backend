package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.UserResource;

/**
 * Converts user entities into user resources.
 */
public final class UserResourceFromEntityAssembler {
  private UserResourceFromEntityAssembler() {
  }

  /**
   * Converts the given user entity into a resource.
   *
   * @param entity user entity
   * @return user resource
   */
  public static UserResource toResourceFromEntity(final User entity) {
    var roles = entity.getRoles().stream()
        .map(Role::getStringName)
        .toList();
    return new UserResource(
        entity.getId(),
        entity.getEmail(),
        entity.getFirstName(),
        entity.getLastName(),
        entity.getPhone(),
        roles
    );
  }
}
