package pe.edu.upc.travelmatch.agencies.application.internal.queryservices;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgenciesQuery;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyQueryServiceImpl Tests")
public class AgencyQueryServiceImplTest {
    @Mock
    private AgencyRepository agencyRepository;

    @InjectMocks
    private AgencyQueryServiceImpl agencyQueryService;

    @Test
    @DisplayName("Debe retornar la lista completa de agencias")
    void handle_getAllAgencies_returnsList() {
        // --- Arrange ---
        GetAllAgenciesQuery query = new GetAllAgenciesQuery();
        when(agencyRepository.findAll()).thenReturn(List.of(mock(Agency.class), mock(Agency.class)));

        // --- Act ---
        List<Agency> result = agencyQueryService.handle(query);

        // --- Assert ---
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(agencyRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar una agencia por su ID")
    void handle_getAgencyById_returnsOptional() {
        // --- Arrange ---
        GetAgencyByIdQuery query = new GetAgencyByIdQuery(1L);
        when(agencyRepository.findById(1L)).thenReturn(Optional.of(mock(Agency.class)));

        // --- Act ---
        Optional<Agency> result = agencyQueryService.handle(query);

        // --- Assert ---
        assertTrue(result.isPresent());
        verify(agencyRepository).findById(1L);
    }
}
