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
@DisplayName("UpdateAgencyDocumentCommandFromResourceAssembler")
public class UpdateAgencyDocumentCommandAssemblerTests {
    @Test
    @DisplayName("toCommandFromResource() maps all UpdateAgencyDocumentResource fields to command")
    void toCommandFromResource_validResource_allFieldsMapped() {
        // Arrange
        UpdateAgencyDocumentResource resource = new UpdateAgencyDocumentResource(
                3L, "LICENCIA", "http://example.com/lic.pdf", "Operating license"
        );

        // Act
        UpdateAgencyDocumentCommand command =
                UpdateAgencyDocumentCommandFromResourceAssembler.toCommandFromResource(resource);

        // Assert
        assertAll(
                () -> assertEquals(3L,                           command.id()),
                () -> assertEquals("LICENCIA",                   command.documentType()),
                () -> assertEquals("http://example.com/lic.pdf", command.documentUrl()),
                () -> assertEquals("Operating license",          command.description())
        );
    }

    @Test
    @DisplayName("toCommandFromResource() returns non-null command")
    void toCommandFromResource_validResource_returnsNonNull() {
        // Arrange
        UpdateAgencyDocumentResource resource = new UpdateAgencyDocumentResource(
                1L, "RUC", "http://example.com/ruc.pdf", null
        );

        // Act
        UpdateAgencyDocumentCommand command =
                UpdateAgencyDocumentCommandFromResourceAssembler.toCommandFromResource(resource);

        // Assert
        assertNotNull(command);
    }
}

