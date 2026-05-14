package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.DestinationResource;

/**
 * Unit tests for {@link UpdateDestinationCommandFromResourceAssembler}.
 *
 * <p>This class validates the proper mapping from a DestinationResource and an ID to an
 * UpdateDestinationCommand.
 */
class UpdateDestinationCommandFromResourceAssemblerTest {

  @Test
  @DisplayName(
      "toCommandFromResource should map ID and DestinationResource to UpdateDestinationCommand"
          + " correctly")
  void toCommandFromResource_ShouldMapCorrectly() {
    // Arrange
    var resource =
        new DestinationResource(
            1L, "Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France");

    // Act
    var command = UpdateDestinationCommandFromResourceAssembler.toCommandFromResource(1L, resource);

    // Assert
    assertNotNull(command);
    assertEquals(1L, command.destinationId());
    assertEquals("Paris", command.name());
    assertEquals("123 Street", command.address());
    assertEquals("District 1", command.district());
    assertEquals("Paris", command.city());
    assertEquals("Ile-de-France", command.state());
    assertEquals("France", command.country());
  }
}
