package pe.edu.upc.travelmatch.agencies.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.application.internal.queryservices.AgencyQueryServiceImpl;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgenciesQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyQueryServiceImpl")
class AgencyQueryServiceTests {
  @Mock private AgencyRepository agencyRepository;

  @InjectMocks private AgencyQueryServiceImpl agencyQueryService;

  private Agency agency;

  @BeforeEach
  void arrange_agency() {
    agency =
        new Agency(
            new AgencyName("Tour Perú"), "desc", "12345678901", "info@tp.com", "999888777", 1L);
  }

  @Test
  @DisplayName("handle(GetAgencyByIdQuery) returns agency when it exists")
  void handle_existingId_returnsAgency() {
    // Arrange
    GetAgencyByIdQuery query = new GetAgencyByIdQuery(1L);
    when(agencyRepository.findById(1L)).thenReturn(Optional.of(agency));

    // Act
    Optional<Agency> result = agencyQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(agency, result.get());
  }

  @Test
  @DisplayName("handle(GetAgencyByIdQuery) returns empty when agency not found")
  void handle_nonExistingId_returnsEmpty() {
    // Arrange
    GetAgencyByIdQuery query = new GetAgencyByIdQuery(99L);
    when(agencyRepository.findById(99L)).thenReturn(Optional.empty());

    // Act
    Optional<Agency> result = agencyQueryService.handle(query);

    // Assert
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("handle(GetAllAgenciesQuery) returns all agencies from repository")
  void handle_getAllAgencies_returnsList() {
    // Arrange
    GetAllAgenciesQuery query = new GetAllAgenciesQuery();
    when(agencyRepository.findAll()).thenReturn(List.of(agency));

    // Act
    List<Agency> result = agencyQueryService.handle(query);

    // Assert
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("handle(GetAllAgenciesQuery) returns empty list when no agencies exist")
  void handle_getAllAgencies_emptyRepository_returnsEmptyList() {
    // Arrange
    GetAllAgenciesQuery query = new GetAllAgenciesQuery();
    when(agencyRepository.findAll()).thenReturn(List.of());

    // Act
    List<Agency> result = agencyQueryService.handle(query);

    // Assert
    assertTrue(result.isEmpty());
  }
}
