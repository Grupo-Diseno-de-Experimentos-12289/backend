package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.*;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
@DisplayName("UpdateAgencyStaffCommandFromResourceAssembler")
public class UpdateAgencyStaffCommandAssemblerTests {
    @Test
    @DisplayName("toCommandFromResource() maps all UpdateAgencyStaffResource fields to command")
    void toCommandFromResource_validResource_allFieldsMapped() {
        // Arrange
        UpdateAgencyStaffResource resource = new UpdateAgencyStaffResource(
                5L, "Carlos", "López", "carlos@tp.com", "911111111", "Manager"
        );

        // Act
        UpdateAgencyStaffCommand command =
                UpdateAgencyStaffCommandFromResourceAssembler.toCommandFromResource(resource);

        // Assert
        assertAll(
                () -> assertEquals(5L,               command.id()),
                () -> assertEquals("Carlos",         command.firstName()),
                () -> assertEquals("López",          command.lastName()),
                () -> assertEquals("carlos@tp.com",  command.email()),
                () -> assertEquals("911111111",      command.phone()),
                () -> assertEquals("Manager",        command.position())
        );
    }

    @Test
    @DisplayName("toCommandFromResource() returns non-null command")
    void toCommandFromResource_validResource_returnsNonNull() {
        // Arrange
        UpdateAgencyStaffResource resource = new UpdateAgencyStaffResource(
                1L, "Ana", "Gómez", "ana@tp.com", "912345678", "Guide"
        );

        // Act
        UpdateAgencyStaffCommand command =
                UpdateAgencyStaffCommandFromResourceAssembler.toCommandFromResource(resource);

        // Assert
        assertNotNull(command);
    }
}
