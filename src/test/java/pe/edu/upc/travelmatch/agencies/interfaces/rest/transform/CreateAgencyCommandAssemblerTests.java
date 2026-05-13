package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.CreateAgencyResource;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAgencyCommandFromResourceAssembler")
public class CreateAgencyCommandAssemblerTests {
  @Test
  @DisplayName("toCommandFromResource() maps all CreateAgencyResource fields to command")
  void toCommandFromResource_validResource_allFieldsMapped() {
    // Arrange
    CreateAgencyResource resource =
        new CreateAgencyResource(
            "Tour Perú", "Great tours", "12345678901", "info@tp.com", "999888777", 1L);

    // Act
    CreateAgencyCommand command =
        CreateAgencyCommandFromResourceAssembler.toCommandFromResource(resource);

    // Assert
    assertAll(
        () -> assertEquals("Tour Perú", command.name()),
        () -> assertEquals("Great tours", command.description()),
        () -> assertEquals("12345678901", command.ruc()),
        () -> assertEquals("info@tp.com", command.contactEmail()),
        () -> assertEquals("999888777", command.contactPhone()),
        () -> assertEquals(1L, command.userId()));
  }

  @Test
  @DisplayName("toCommandFromResource() returns non-null command")
  void toCommandFromResource_validResource_returnsNonNull() {
    // Arrange
    CreateAgencyResource resource =
        new CreateAgencyResource("Agency", "desc", "11111111111", "a@b.com", "911111111", 2L);

    // Act
    CreateAgencyCommand command =
        CreateAgencyCommandFromResourceAssembler.toCommandFromResource(resource);

    // Assert
    assertNotNull(command);
  }
}
