package pe.edu.upc.travelmatch.agencies.application.internal.queryservices;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyDocumentByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyDocumentsByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyDocumentRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyDocumentQueryServiceImpl Tests")
public class AgencyDocumentQueryServiceImplTest {
    @Mock
    private AgencyDocumentRepository agencyDocumentRepository;
    @Mock
    private AgencyRepository agencyRepository;

    @InjectMocks
    private AgencyDocumentQueryServiceImpl agencyDocumentQueryService;

    @Test
    @DisplayName("Debe retornar documentos de una agencia válida")
    void handle_getAllByAgencyId_agencyExists_returnsList() {
        // --- Arrange ---
        GetAllAgencyDocumentsByAgencyIdQuery query = new GetAllAgencyDocumentsByAgencyIdQuery(1L);
        when(agencyRepository.existsById(1L)).thenReturn(true);
        when(agencyDocumentRepository.findByAgencyId(1L)).thenReturn(List.of(mock(AgencyDocument.class)));

        // --- Act ---
        List<AgencyDocument> result = agencyDocumentQueryService.handle(query);

        // --- Assert ---
        assertFalse(result.isEmpty());
        verify(agencyDocumentRepository).findByAgencyId(1L);
    }

    @Test
    @DisplayName("Debe retornar un documento por ID")
    void handle_getById_exists_returnsOptional() {
        // --- Arrange ---
        GetAgencyDocumentByIdQuery query = new GetAgencyDocumentByIdQuery(1L);
        when(agencyDocumentRepository.findById(1L)).thenReturn(Optional.of(mock(AgencyDocument.class)));

        // --- Act ---
        Optional<AgencyDocument> result = agencyDocumentQueryService.handle(query);

        // --- Assert ---
        assertTrue(result.isPresent());
        verify(agencyDocumentRepository).findById(1L);
    }
}
