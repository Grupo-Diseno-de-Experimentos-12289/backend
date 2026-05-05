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
@DisplayName("CreateAgencyDocumentCommandFromResourceAssembler")
public class CreateAgencyDocumentCommandAssemblerTests {
    @Test
    @DisplayName("toCommandFromResource() maps all CreateAgencyDocumentResource fields to command")
    void toCommandFromResource_validResource_allFieldsMapped() {
        // Arrange
        Long agencyId = 1L;
        CreateAgencyDocumentResource resource = new CreateAgencyDocumentResource(
                "RUC", "http://example.com/ruc.pdf", "Tax ID"
        );

        // Act
        CreateAgencyDocumentCommand command =
                CreateAgencyDocumentCommandFromResourceAssembler.toCommandFromResource(agencyId, resource);

        // Assert
        assertAll(
                () -> assertEquals(1L,                           command.agencyId()),
                () -> assertEquals("RUC",                        command.documentType()),
                () -> assertEquals("http://example.com/ruc.pdf", command.documentUrl()),
                () -> assertEquals("Tax ID",                     command.description())
        );
    }

    @Test
    @DisplayName("toCommandFromResource() propagates agencyId path variable correctly")
    void toCommandFromResource_differentAgencyId_agencyIdPropagated() {
        // Arrange
        Long agencyId = 7L;
        CreateAgencyDocumentResource resource = new CreateAgencyDocumentResource(
                "LICENCIA", "http://example.com/lic.pdf", null
        );

        // Act
        CreateAgencyDocumentCommand command =
                CreateAgencyDocumentCommandFromResourceAssembler.toCommandFromResource(agencyId, resource);

        // Assert
        assertEquals(7L, command.agencyId());
    }
}
