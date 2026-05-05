package pe.edu.upc.travelmatch.iam.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.iam.interfaces.rest.resources.SignUpResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link SignUpCommandFromResourceAssembler}.
 *
 * This class validates the proper mapping from a SignUpResource to a SignUpCommand,
 * including correct handling of the user roles.
 */
class SignUpCommandFromResourceAssemblerTest {

    @Test
    @DisplayName("toCommandFromResource should map SignUpResource to SignUpCommand correctly when roles are provided")
    void toCommandFromResource_ShouldMapWithRoles() {
        // Arrange
        var resource = new SignUpResource("user@example.com", "password", "John", "Doe", "123", List.of("ROLE_TOURIST"));

        // Act
        var command = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);

        // Assert
        assertNotNull(command);
        assertEquals("user@example.com", command.email());
        assertEquals("password", command.password());
        assertEquals("John", command.firstName());
        assertEquals("Doe", command.lastName());
        assertEquals("123", command.phone());
        assertEquals(1, command.roles().size());
    }

    @Test
    @DisplayName("toCommandFromResource should map SignUpResource to SignUpCommand correctly when roles are null")
    void toCommandFromResource_ShouldMapWithNullRoles() {
        // Arrange
        var resource = new SignUpResource("user@example.com", "password", "John", "Doe", "123", null);

        // Act
        var command = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);

        // Assert
        assertNotNull(command);
        assertTrue(command.roles().isEmpty());
    }
}
