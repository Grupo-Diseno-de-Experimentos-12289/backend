package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import java.util.ArrayList;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.SignUpResource;

/**
 * Converts sign-up resources into sign-up commands.
 */
public final class SignUpCommandFromResourceAssembler {
  private SignUpCommandFromResourceAssembler() {
  }

  /**
   * Converts the given sign-up resource into a command.
   *
   * @param resource sign-up resource
   * @return sign-up command
   */
  public static SignUpCommand toCommandFromResource(
      final SignUpResource resource
  ) {
    var roles = resource.roles() != null
        ? resource.roles().stream()
            .map(Role::toRoleFromName)
            .toList()
        : new ArrayList<Role>();
    return new SignUpCommand(
        resource.email(),
        resource.password(),
        resource.firstName(),
        resource.lastName(),
        resource.phone(),
        roles
    );
  }
}
