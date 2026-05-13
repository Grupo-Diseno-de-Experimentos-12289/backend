package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import pe.edu.upc.travelmatch.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.SignInResource;

/** SignInCommandFromResourceAssembler type. */
public class SignInCommandFromResourceAssembler {
  /** To command from resource. */
  public static SignInCommand toCommandFromResource(SignInResource resource) {
    return new SignInCommand(resource.email(), resource.password());
  }
}
