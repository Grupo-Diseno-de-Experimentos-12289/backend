package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyStaffCommand;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.UpdateAgencyStaffResource;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateAgencyStaffCommandFromResourceAssembler")
public class UpdateAgencyStaffCommandAssemblerTests {
  @Test
  @DisplayName("toCommandFromResource() maps all UpdateAgencyStaffResource fields to command")
  void toCommandFromResource_validResource_allFieldsMapped() {
    // Arrange
    UpdateAgencyStaffResource resource =
        new UpdateAgencyStaffResource(
            5L, "Carlos", "López", "carlos@tp.com", "911111111", "Manager");

    // Act
    UpdateAgencyStaffCommand command =
        UpdateAgencyStaffCommandFromResourceAssembler.toCommandFromResource(resource);

    // Assert
    assertAll(
        () -> assertEquals(5L, command.id()),
        () -> assertEquals("Carlos", command.firstName()),
        () -> assertEquals("López", command.lastName()),
        () -> assertEquals("carlos@tp.com", command.email()),
        () -> assertEquals("911111111", command.phone()),
        () -> assertEquals("Manager", command.position()));
  }

  @Test
  @DisplayName("toCommandFromResource() returns non-null command")
  void toCommandFromResource_validResource_returnsNonNull() {
    // Arrange
    UpdateAgencyStaffResource resource =
        new UpdateAgencyStaffResource(1L, "Ana", "Gómez", "ana@tp.com", "912345678", "Guide");

    // Act
    UpdateAgencyStaffCommand command =
        UpdateAgencyStaffCommandFromResourceAssembler.toCommandFromResource(resource);

    // Assert
    assertNotNull(command);
  }
}
