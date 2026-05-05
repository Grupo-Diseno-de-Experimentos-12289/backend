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

public class AgencyDocumentQueryServiceTests {
    @Mock private AgencyDocumentRepository agencyDocumentRepository;
    @Mock private AgencyRepository         agencyRepository;

    @InjectMocks
    private AgencyDocumentQueryServiceImpl agencyDocumentQueryService;

    private Agency        agency;
    private AgencyDocument document;

    @BeforeEach
    void arrange_fixtures() {
        agency   = new Agency(new AgencyName("Tour Perú"), "desc",
                "12345678901", "info@tp.com", "999888777", 1L);
        document = new AgencyDocument(agency, "RUC",
                "http://example.com/ruc.pdf", "Tax ID document");
    }

    @Test
    @DisplayName("handle(GetAllAgencyDocumentsByAgencyIdQuery) returns documents for valid agency")
    void handle_validAgency_returnsDocumentList() {
        // Arrange
        GetAllAgencyDocumentsByAgencyIdQuery query =
                new GetAllAgencyDocumentsByAgencyIdQuery(1L);
        when(agencyRepository.existsById(1L)).thenReturn(true);
        when(agencyDocumentRepository.findByAgencyId(1L)).thenReturn(List.of(document));

        // Act
        List<AgencyDocument> result = agencyDocumentQueryService.handle(query);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("handle(GetAllAgencyDocumentsByAgencyIdQuery) returns empty list when agency not found")
    void handle_agencyNotFound_returnsEmptyList() {
        // Arrange
        GetAllAgencyDocumentsByAgencyIdQuery query =
                new GetAllAgencyDocumentsByAgencyIdQuery(99L);
        when(agencyRepository.existsById(99L)).thenReturn(false);

        // Act
        List<AgencyDocument> result = agencyDocumentQueryService.handle(query);

        // Assert
        assertTrue(result.isEmpty());
        verify(agencyDocumentRepository, never()).findByAgencyId(any());
    }

    @Test
    @DisplayName("handle(GetAgencyDocumentByIdQuery) returns document when found")
    void handle_existingDocumentId_returnsDocument() {
        // Arrange
        GetAgencyDocumentByIdQuery query = new GetAgencyDocumentByIdQuery(1L);
        when(agencyDocumentRepository.findById(1L)).thenReturn(Optional.of(document));

        // Act
        Optional<AgencyDocument> result = agencyDocumentQueryService.handle(query);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(document, result.get());
    }

    @Test
    @DisplayName("handle(GetAgencyDocumentByIdQuery) returns empty when document not found")
    void handle_nonExistingDocumentId_returnsEmpty() {
        // Arrange
        GetAgencyDocumentByIdQuery query = new GetAgencyDocumentByIdQuery(99L);
        when(agencyDocumentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<AgencyDocument> result = agencyDocumentQueryService.handle(query);

        // Assert
        assertTrue(result.isEmpty());
    }
}

