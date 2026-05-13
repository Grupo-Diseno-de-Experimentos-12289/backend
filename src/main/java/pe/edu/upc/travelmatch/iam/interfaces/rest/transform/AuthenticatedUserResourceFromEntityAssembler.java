package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import java.util.Set;
import java.util.stream.Collectors;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.AuthenticatedUserResource;

/**
 * Converts a user entity into an authenticated user resource.
 */
public final class AuthenticatedUserResourceFromEntityAssembler {
  private AuthenticatedUserResourceFromEntityAssembler() {
  }

  /**
   * Converts the given user entity into an authenticated user resource.
   *
   * @param user entity to convert
   * @param token authentication token
   * @return authenticated user resource
   */
  public static AuthenticatedUserResource toResourceFromEntity(
      final User user,
      final String token
  ) {
    Set<String> roleNames = user.getRoles().stream()
        .map(Role::getStringName)
        .collect(Collectors.toSet());
    return new AuthenticatedUserResource(
        user.getId(),
        user.getEmail(),
        token,
        roleNames
    );
  }
}

