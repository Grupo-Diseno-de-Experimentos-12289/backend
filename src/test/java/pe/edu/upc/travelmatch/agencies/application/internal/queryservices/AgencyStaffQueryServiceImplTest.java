package pe.edu.upc.travelmatch.agencies.application.internal.queryservices;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyStaffByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyStaffByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyStaffRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyStaffQueryServiceImpl Tests")
class AgencyStaffQueryServiceImplTest {
  @Mock private AgencyStaffRepository agencyStaffRepository;
  @Mock private AgencyRepository agencyRepository;

  @InjectMocks private AgencyStaffQueryServiceImpl agencyStaffQueryService;

  @Test
  @DisplayName("Debe retornar lista de staff si la agencia existe")
  void handle_getAllByAgencyId_agencyExists_returnsList() {
    // --- Arrange ---
    GetAllAgencyStaffByAgencyIdQuery query = new GetAllAgencyStaffByAgencyIdQuery(1L);
    when(agencyRepository.existsById(1L)).thenReturn(true);
    when(agencyStaffRepository.findByAgencyId(1L)).thenReturn(List.of(mock(AgencyStaff.class)));

    // --- Act ---
    List<AgencyStaff> result = agencyStaffQueryService.handle(query);

    // --- Assert ---
    assertFalse(result.isEmpty());
    verify(agencyStaffRepository).findByAgencyId(1L);
  }

  @Test
  @DisplayName("Debe retornar staff por su ID")
  void handle_getById_exists_returnsOptional() {
    // --- Arrange ---
    GetAgencyStaffByIdQuery query = new GetAgencyStaffByIdQuery(1L);
    when(agencyStaffRepository.findById(1L)).thenReturn(Optional.of(mock(AgencyStaff.class)));

    // --- Act ---
    Optional<AgencyStaff> result = agencyStaffQueryService.handle(query);

    // --- Assert ---
    assertTrue(result.isPresent());
    verify(agencyStaffRepository).findById(1L);
  }
}
