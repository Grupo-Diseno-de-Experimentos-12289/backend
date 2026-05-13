package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyDocumentResourceFromEntityAssembler")
public class AgencyDocumentResourceAssemblerTests {

  @Mock private AgencyDocument agencyDocument;

  @Mock private Agency agency;

  @Test
  @DisplayName("toResourceFromEntity() maps all document fields to AgencyDocumentResource")
  void toResourceFromEntity_validDocument_allFieldsMapped() {
    // Arrange
    when(agencyDocument.getId()).thenReturn(10L);
    when(agencyDocument.getAgency()).thenReturn(agency);
    when(agency.getId()).thenReturn(1L);
    when(agencyDocument.getDocumentType()).thenReturn("RUC");
    when(agencyDocument.getDocumentUrl()).thenReturn("http://example.com/ruc.pdf");
    when(agencyDocument.getDescription()).thenReturn("Tax ID document");

    // Act
    var resource = AgencyDocumentResourceFromEntityAssembler.toResourceFromEntity(agencyDocument);

    // Assert
    assertAll(
        () -> assertEquals(10L, resource.id()),
        () -> assertEquals(1L, resource.agencyId()),
        () -> assertEquals("RUC", resource.documentType()),
        () -> assertEquals("http://example.com/ruc.pdf", resource.documentUrl()),
        () -> assertEquals("Tax ID document", resource.description()));
  }

  @Test
  @DisplayName("toResourceFromEntity() returns non-null resource")
  void toResourceFromEntity_validDocument_returnsNonNull() {
    // Arrange
    when(agencyDocument.getId()).thenReturn(5L);
    when(agencyDocument.getAgency()).thenReturn(agency);
    when(agency.getId()).thenReturn(2L);
    when(agencyDocument.getDocumentType()).thenReturn("LICENCIA");
    when(agencyDocument.getDocumentUrl()).thenReturn("http://example.com/lic.pdf");
    when(agencyDocument.getDescription()).thenReturn(null);

    // Act
    var resource = AgencyDocumentResourceFromEntityAssembler.toResourceFromEntity(agencyDocument);

    // Assert
    assertNotNull(resource);
  }
}
