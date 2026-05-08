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
@DisplayName("AgencyResourceFromAgencyAssembler")
public class AgencyResourceAssemblerTests {
    @Mock private Agency agency;

    @Test
    @DisplayName("toResourceFromEntity() maps all agency fields to AgencyResource correctly")
    void toResourceFromEntity_validAgency_allFieldsMapped() {
        // Arrange
        AgencyName name = new AgencyName("Tour Perú");
        when(agency.getId()).thenReturn(1L);
        when(agency.getName()).thenReturn(name);
        when(agency.getDescription()).thenReturn("Great tours");
        when(agency.getRuc()).thenReturn("12345678901");
        when(agency.getContactEmail()).thenReturn("info@tp.com");
        when(agency.getContactPhone()).thenReturn("999888777");

        // Act
        var resource = AgencyResourceFromAgencyAssembler.toResourceFromEntity(agency);

        // Assert
        assertAll(
                () -> assertEquals(1L,               resource.id()),
                () -> assertEquals("Tour Perú",      resource.name()),
                () -> assertEquals("Great tours",    resource.description()),
                () -> assertEquals("12345678901",    resource.ruc()),
                () -> assertEquals("info@tp.com",    resource.contactEmail()),
                () -> assertEquals("999888777",      resource.contactPhone())
        );
    }

    @Test
    @DisplayName("toResourceFromEntity() returns non-null resource for a valid agency")
    void toResourceFromEntity_validAgency_returnsNonNull() {
        // Arrange
        when(agency.getId()).thenReturn(2L);
        when(agency.getName()).thenReturn(new AgencyName("Another Agency"));
        when(agency.getDescription()).thenReturn("desc");
        when(agency.getRuc()).thenReturn("11111111111");
        when(agency.getContactEmail()).thenReturn("a@a.com");
        when(agency.getContactPhone()).thenReturn("111111111");

        // Act
        var resource = AgencyResourceFromAgencyAssembler.toResourceFromEntity(agency);

        // Assert
        assertNotNull(resource);
    }
}
