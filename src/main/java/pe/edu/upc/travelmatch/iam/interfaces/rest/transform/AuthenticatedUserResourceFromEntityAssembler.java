package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import java.util.Set;
import java.util.stream.Collectors;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.AuthenticatedUserResource;

/** AuthenticatedUserResourceFromEntityAssembler type. */
public class AuthenticatedUserResourceFromEntityAssembler {
  /** To resource from entity. */
  public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
    Set<String> roleNames =
        user.getRoles().stream().map(Role::getStringName).collect(Collectors.toSet());
    return new AuthenticatedUserResource(user.getId(), user.getEmail(), token, roleNames);
  }
}
