package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import pe.edu.upc.travelmatch.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.SignInResource;

/**
 * Converts sign-in resources into sign-in commands.
 */
public final class SignInCommandFromResourceAssembler {
  private SignInCommandFromResourceAssembler() {
  }

  /**
   * Converts the given sign-in resource into a command.
   *
   * @param resource sign-in resource
   * @return sign-in command
   */
  public static SignInCommand toCommandFromResource(
      final SignInResource resource
  ) {
    return new SignInCommand(resource.email(), resource.password());
  }
}
