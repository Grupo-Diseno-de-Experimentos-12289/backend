package pe.edu.upc.travelmatch.agencies.domain.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.application.internal.commandservices.AgencyDocumentCommandServiceImpl;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyDocumentRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyDocumentCommandServiceImpl")
class AgencyDocumentCommandServiceTests {
  @Mock private AgencyDocumentRepository agencyDocumentRepository;
  @Mock private AgencyRepository agencyRepository;

  @InjectMocks private AgencyDocumentCommandServiceImpl agencyDocumentCommandService;

  private Agency agency;
  private AgencyDocument document;

  @BeforeEach
  void arrange_fixtures() {
    agency =
        new Agency(
            new AgencyName("Tour Perú"), "desc", "12345678901", "contact@tp.com", "999888777", 1L);
    document = new AgencyDocument(agency, "RUC", "http://example.com/ruc.pdf", "Tax ID document");
  }

  @Test
  @DisplayName("handle(CreateAgencyDocumentCommand) creates document when agency exists")
  void handle_createDocument_agencyExists_documentIsSaved() {
    // Arrange
    CreateAgencyDocumentCommand command =
        new CreateAgencyDocumentCommand(1L, "RUC", "http://example.com/ruc.pdf", "Tax ID");
    when(agencyRepository.findById(1L)).thenReturn(Optional.of(agency));
    when(agencyDocumentRepository.save(any())).thenReturn(document);

    // Act
    var result = agencyDocumentCommandService.handle(command);

    // Assert
    assertTrue(result.isPresent());
    verify(agencyDocumentRepository).save(any(AgencyDocument.class));
  }

  @Test
  @DisplayName("handle(CreateAgencyDocumentCommand) throws when agency not found")
  void handle_createDocument_agencyNotFound_throwsIllegalArgument() {
    // Arrange
    CreateAgencyDocumentCommand command =
        new CreateAgencyDocumentCommand(99L, "RUC", "http://example.com/ruc.pdf", "Tax ID");
    when(agencyRepository.findById(99L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> agencyDocumentCommandService.handle(command));

    verify(agencyDocumentRepository, never()).save(any());
  }

  @Test
  @DisplayName("handle(UpdateAgencyDocumentCommand) updates fields and returns document")
  void handle_updateDocument_exists_updatedFieldsPersisted() {
    // Arrange
    UpdateAgencyDocumentCommand command =
        new UpdateAgencyDocumentCommand(
            1L, "LICENCIA", "http://new.com/lic.pdf", "Operating license");
    when(agencyDocumentRepository.findById(1L)).thenReturn(Optional.of(document));
    when(agencyDocumentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    // Act
    var result = agencyDocumentCommandService.handle(command);

    // Assert
    assertTrue(result.isPresent());
    assertAll(
        () -> assertEquals("LICENCIA", result.get().getDocumentType()),
        () -> assertEquals("http://new.com/lic.pdf", result.get().getDocumentUrl()),
        () -> assertEquals("Operating license", result.get().getDescription()));
  }

  @Test
  @DisplayName("handle(UpdateAgencyDocumentCommand) returns empty when document not found")
  void handle_updateDocument_notFound_returnsEmpty() {
    // Arrange
    UpdateAgencyDocumentCommand command =
        new UpdateAgencyDocumentCommand(999L, "LICENCIA", "http://new.com/lic.pdf", "desc");
    when(agencyDocumentRepository.findById(999L)).thenReturn(Optional.empty());

    // Act
    var result = agencyDocumentCommandService.handle(command);

    // Assert
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("handle(DeleteAgencyDocumentCommand) deletes document when it exists")
  void handle_deleteDocument_exists_deletesSuccessfully() {
    // Arrange
    DeleteAgencyDocumentCommand command = new DeleteAgencyDocumentCommand(1L);
    when(agencyDocumentRepository.existsById(1L)).thenReturn(true);

    // Act
    assertDoesNotThrow(() -> agencyDocumentCommandService.handle(command));

    // Assert
    verify(agencyDocumentRepository).deleteById(1L);
  }

  @Test
  @DisplayName("handle(DeleteAgencyDocumentCommand) throws when document not found")
  void handle_deleteDocument_notFound_throwsIllegalArgument() {
    // Arrange
    DeleteAgencyDocumentCommand command = new DeleteAgencyDocumentCommand(999L);
    when(agencyDocumentRepository.existsById(999L)).thenReturn(false);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> agencyDocumentCommandService.handle(command));

    verify(agencyDocumentRepository, never()).deleteById(any());
  }
}
