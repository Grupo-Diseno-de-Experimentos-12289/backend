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
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAgencyStaffCommandFromResourceAssembler")
public class CreateAgencyStaffCommandAssemblerTests {
    @Test
    @DisplayName("toCommandFromResource() maps all CreateAgencyStaffResource fields to command")
    void toCommandFromResource_validResource_allFieldsMapped() {
        // Arrange
        Long agencyId = 1L;
        CreateAgencyStaffResource resource = new CreateAgencyStaffResource(
                "Juan", "Pérez", "juan@tp.com", "987654321", "Guide"
        );

        // Act
        CreateAgencyStaffCommand command =
                CreateAgencyStaffCommandFromResourceAssembler.toCommandFromResource(agencyId, resource);

        // Assert
        assertAll(
                () -> assertEquals(1L,            command.agencyId()),
                () -> assertEquals("Juan",        command.firstName()),
                () -> assertEquals("Pérez",       command.lastName()),
                () -> assertEquals("juan@tp.com", command.email()),
                () -> assertEquals("987654321",   command.phone()),
                () -> assertEquals("Guide",       command.position())
        );
    }

    @Test
    @DisplayName("toCommandFromResource() propagates the agencyId path variable correctly")
    void toCommandFromResource_differentAgencyId_agencyIdPropagated() {
        // Arrange
        Long agencyId = 42L;
        CreateAgencyStaffResource resource = new CreateAgencyStaffResource(
                "Ana", "Gómez", "ana@tp.com", "912345678", "Manager"
        );

        // Act
        CreateAgencyStaffCommand command =
                CreateAgencyStaffCommandFromResourceAssembler.toCommandFromResource(agencyId, resource);

        // Assert
        assertEquals(42L, command.agencyId());
    }
}
