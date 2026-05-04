package pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.geolocationv2.interfaces.rest.resources.CreateDestinationResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Implementation of the tests for CreateDestinationCommandFromResourceAssembler.
 *
 * <p>This class validates the proper mapping from a CreateDestinationResource to
 * a CreateDestinationCommand.</p>
 */
class CreateDestinationCommandFromResourceAssemblerTest {

    @Test
    @DisplayName("toCommandFromResource should map CreateDestinationResource to CreateDestinationCommand correctly")
    void toCommandFromResource_ShouldMapCorrectly() {
        // Arrange
        var resource = new CreateDestinationResource("Paris", "123 Street", "District 1", "Paris", "Ile-de-France", "France");

        // Act
        var command = CreateDestinationCommandFromResourceAssembler.toCommandFromResource(resource);

        // Assert
        assertNotNull(command);
        assertEquals("Paris", command.name());
        assertEquals("123 Street", command.address());
        assertEquals("District 1", command.district());
        assertEquals("Paris", command.city());
        assertEquals("Ile-de-France", command.state());
        assertEquals("France", command.country());
    }
}
