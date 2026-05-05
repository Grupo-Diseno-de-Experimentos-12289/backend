package pe.edu.upc.travelmatch.agencies.application.internal.commandservices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyDocumentRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyDocumentCommandServiceImpl Tests")
public class AgencyDocumentCommandServiceImplTest {
    @Mock
    private AgencyDocumentRepository agencyDocumentRepository;
    @Mock
    private AgencyRepository agencyRepository;

    @InjectMocks
    private AgencyDocumentCommandServiceImpl agencyDocumentCommandService;

    private Agency agency;
    private AgencyDocument existingDocument;

    @BeforeEach
    void setUp() {
        agency = new Agency(new AgencyName("Tour Perú"), "desc", "12345678901", "info@tp.com", "999888777", 1L);
        existingDocument = new AgencyDocument(agency, "LICENSE", "http://docs.com/lic.pdf", "Business License");
    }

    @Test
    @DisplayName("Debe crear un documento si la agencia existe")
    void handle_createDocument_agencyExists_returnsDocument() {
        // --- Arrange ---
        CreateAgencyDocumentCommand command = new CreateAgencyDocumentCommand(1L, "LICENSE", "http://docs.com/lic.pdf", "Business License");
        when(agencyRepository.findById(1L)).thenReturn(Optional.of(agency));
        when(agencyDocumentRepository.save(any(AgencyDocument.class))).thenAnswer(inv -> inv.getArgument(0));

        // --- Act ---
        Optional<AgencyDocument> result = agencyDocumentCommandService.handle(command);

        // --- Assert ---
        assertTrue(result.isPresent(), "El documento debe ser creado exitosamente");
        verify(agencyDocumentRepository, times(1)).save(any(AgencyDocument.class));
    }

    @Test
    @DisplayName("Debe actualizar el documento exitosamente")
    void handle_updateDocument_valid_returnsUpdatedDocument() {
        // --- Arrange ---
        UpdateAgencyDocumentCommand command = new UpdateAgencyDocumentCommand(1L, "NEW_TYPE", "http://docs.com/new.pdf", "New Desc");
        when(agencyDocumentRepository.findById(1L)).thenReturn(Optional.of(existingDocument));
        when(agencyDocumentRepository.save(existingDocument)).thenReturn(existingDocument);

        // --- Act ---
        Optional<AgencyDocument> result = agencyDocumentCommandService.handle(command);

        // --- Assert ---
        assertTrue(result.isPresent());
        assertEquals("NEW_TYPE", result.get().getDocumentType());
        verify(agencyDocumentRepository).save(existingDocument);
    }

    @Test
    @DisplayName("Debe eliminar el documento si existe")
    void handle_deleteDocument_exists_deletesSuccessfully() {
        // --- Arrange ---
        DeleteAgencyDocumentCommand command = new DeleteAgencyDocumentCommand(1L);
        when(agencyDocumentRepository.existsById(1L)).thenReturn(true);

        // --- Act ---
        assertDoesNotThrow(() -> agencyDocumentCommandService.handle(command));

        // --- Assert ---
        verify(agencyDocumentRepository).deleteById(1L);
    }
}
