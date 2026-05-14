package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import java.util.ArrayList;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.travelmatch.iam.domain.model.entities.Role;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.SignUpResource;

/** SignUpCommandFromResourceAssembler type. */
public class SignUpCommandFromResourceAssembler {
  /** To command from resource. */
  public static SignUpCommand toCommandFromResource(SignUpResource resource) {
    var roles =
        resource.roles() != null
            ? resource.roles().stream().map(Role::toRoleFromName).toList()
            : new ArrayList<Role>();
    return new SignUpCommand(
        resource.email(),
        resource.password(),
        resource.firstName(),
        resource.lastName(),
        resource.phone(),
        roles);
  }
}
