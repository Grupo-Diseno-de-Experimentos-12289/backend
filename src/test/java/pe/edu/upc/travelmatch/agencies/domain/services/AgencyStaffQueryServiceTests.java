package pe.edu.upc.travelmatch.agencies.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.application.internal.queryservices.AgencyDocumentQueryServiceImpl;
import pe.edu.upc.travelmatch.agencies.application.internal.queryservices.AgencyQueryServiceImpl;
import pe.edu.upc.travelmatch.agencies.application.internal.queryservices.AgencyStaffQueryServiceImpl;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.*;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyDocumentRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyStaffRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyStaffQueryServiceImpl")
public class AgencyStaffQueryServiceTests {
    @Mock private AgencyStaffRepository agencyStaffRepository;
    @Mock private AgencyRepository      agencyRepository;

    @InjectMocks
    private AgencyStaffQueryServiceImpl agencyStaffQueryService;

    private Agency      agency;
    private AgencyStaff staff;

    @BeforeEach
    void arrange_fixtures() {
        agency = new Agency(new AgencyName("Tour Perú"), "desc",
                "12345678901", "info@tp.com", "999888777", 1L);
        staff  = new AgencyStaff(agency, "Juan", "Pérez",
                "juan@tp.com", "987654321", "Guide");
    }

    @Test
    @DisplayName("handle(GetAllAgencyStaffByAgencyIdQuery) returns staff list for valid agency")
    void handle_validAgency_returnsStaffList() {
        // Arrange
        GetAllAgencyStaffByAgencyIdQuery query = new GetAllAgencyStaffByAgencyIdQuery(1L);
        when(agencyRepository.existsById(1L)).thenReturn(true);
        when(agencyStaffRepository.findByAgencyId(1L)).thenReturn(List.of(staff));

        // Act
        List<AgencyStaff> result = agencyStaffQueryService.handle(query);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("handle(GetAllAgencyStaffByAgencyIdQuery) returns empty list when agency not found")
    void handle_agencyNotFound_returnsEmptyList() {
        // Arrange
        GetAllAgencyStaffByAgencyIdQuery query = new GetAllAgencyStaffByAgencyIdQuery(99L);
        when(agencyRepository.existsById(99L)).thenReturn(false);

        // Act
        List<AgencyStaff> result = agencyStaffQueryService.handle(query);

        // Assert
        assertTrue(result.isEmpty());
        verify(agencyStaffRepository, never()).findByAgencyId(any());
    }

    @Test
    @DisplayName("handle(GetAgencyStaffByIdQuery) returns staff when found")
    void handle_existingStaffId_returnsStaff() {
        // Arrange
        GetAgencyStaffByIdQuery query = new GetAgencyStaffByIdQuery(1L);
        when(agencyStaffRepository.findById(1L)).thenReturn(Optional.of(staff));

        // Act
        Optional<AgencyStaff> result = agencyStaffQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(staff, result.get());
    }

    @Test
    @DisplayName("handle(GetAgencyStaffByIdQuery) returns empty when staff not found")
    void handle_nonExistingStaffId_returnsEmpty() {
        // Arrange
        GetAgencyStaffByIdQuery query = new GetAgencyStaffByIdQuery(99L);
        when(agencyStaffRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<AgencyStaff> result = agencyStaffQueryService.handle(query);

        // Assert
        assertTrue(result.isEmpty());
    }
}
