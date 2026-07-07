package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.SignInResource;

/**
 * Unit tests for {@link SignInCommandFromResourceAssembler}.
 *
 * <p>This class validates the proper mapping from a SignInResource to a SignInCommand.
 */
class SignInCommandFromResourceAssemblerTest {

  @Test
  @DisplayName("toCommandFromResource should map SignInResource to SignInCommand correctly")
  void toCommandFromResource_ShouldMapSignInResourceToSignInCommand() {
    // Arrange
    var resource = new SignInResource("user@example.com", "password");

    // Act
    var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);

    // Assert
    assertNotNull(command);
    assertEquals("user@example.com", command.email());
    assertEquals("password", command.password());
  }
}
