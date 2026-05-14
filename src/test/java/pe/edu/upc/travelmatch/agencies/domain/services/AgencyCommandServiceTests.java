package pe.edu.upc.travelmatch.agencies.domain.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.agencies.application.internal.commandservices.AgencyCommandServiceImpl;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.interfaces.acl.AgenciesContextFacade;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyCommandServiceImpl")
class AgencyCommandServiceTests {
  @Mock private AgencyRepository agencyRepository;
  @Mock private AgenciesContextFacade agenciesContextFacade;

  @InjectMocks private AgencyCommandServiceImpl agencyCommandService;

  private Agency existingAgency;

  @BeforeEach
  void arrange_existingAgency() {
    existingAgency =
        new Agency(
            new AgencyName("Tour Perú"),
            "Wonderful tours",
            "12345678901",
            "info@tourperu.com",
            "999888777",
            1L);
  }

  // ── Create ───────────────────────────────────────────

  @Test
  @DisplayName("handle(CreateAgencyCommand) saves agency when all validations pass")
  void handle_createAgency_allValid_agencyIsSaved() {
    // Arrange
    final CreateAgencyCommand command =
        new CreateAgencyCommand(
            "Tour Perú", "Great tours", "12345678901", "info@tourperu.com", "999888777", 1L);
    when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
    when(agencyRepository.existsByUserId(1L)).thenReturn(false);
    when(agencyRepository.existsByRuc("12345678901")).thenReturn(false);
    when(agencyRepository.existsByContactEmail("info@tourperu.com")).thenReturn(false);
    when(agencyRepository.save(any(Agency.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    assertDoesNotThrow(() -> agencyCommandService.handle(command));

    // Assert
    verify(agencyRepository, times(1)).save(any(Agency.class));
  }

  @Test
  @DisplayName("handle(CreateAgencyCommand) saves agency with correct field values")
  void handle_createAgency_savedWithCorrectFields() {
    // Arrange
    final CreateAgencyCommand command =
        new CreateAgencyCommand(
            "Tour Perú", "Great tours", "12345678901", "info@tourperu.com", "999888777", 1L);
    when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
    when(agencyRepository.existsByUserId(1L)).thenReturn(false);
    when(agencyRepository.existsByRuc("12345678901")).thenReturn(false);
    when(agencyRepository.existsByContactEmail("info@tourperu.com")).thenReturn(false);

    ArgumentCaptor<Agency> captor = ArgumentCaptor.forClass(Agency.class);
    when(agencyRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

    // Act
    agencyCommandService.handle(command);

    // Assert
    Agency saved = captor.getValue();
    assertAll(
        () -> assertEquals("Tour Perú", saved.getName().getName()),
        () -> assertEquals("12345678901", saved.getRuc()),
        () -> assertEquals("info@tourperu.com", saved.getContactEmail()),
        () -> assertEquals(1L, saved.getUserId()));
  }

  @Test
  @DisplayName("handle(CreateAgencyCommand) throws when user not found in IAM")
  void handle_createAgency_userNotFound_throwsIllegalArgument() {
    // Arrange
    CreateAgencyCommand command =
        new CreateAgencyCommand(
            "Tour Perú", "desc", "12345678901", "info@tourperu.com", "999888777", 99L);
    when(agenciesContextFacade.existsUserById(99L)).thenReturn(false);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));

    verify(agencyRepository, never()).save(any());
  }

  @Test
  @DisplayName("handle(CreateAgencyCommand) throws when agency already exists for userId")
  void handle_createAgency_duplicateUserId_throwsIllegalArgument() {
    // Arrange
    CreateAgencyCommand command =
        new CreateAgencyCommand(
            "Tour Perú", "desc", "12345678901", "info@tourperu.com", "999888777", 1L);
    when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
    when(agencyRepository.existsByUserId(1L)).thenReturn(true);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));
  }

  @Test
  @DisplayName("handle(CreateAgencyCommand) throws when RUC is already taken")
  void handle_createAgency_duplicateRuc_throwsIllegalArgument() {
    // Arrange
    final CreateAgencyCommand command =
        new CreateAgencyCommand(
            "Tour Perú", "desc", "12345678901", "info@tourperu.com", "999888777", 1L);
    when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
    when(agencyRepository.existsByUserId(1L)).thenReturn(false);
    when(agencyRepository.existsByRuc("12345678901")).thenReturn(true);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));
  }

  @Test
  @DisplayName("handle(CreateAgencyCommand) throws when contact email is already registered")
  void handle_createAgency_duplicateEmail_throwsIllegalArgument() {
    // Arrange
    final CreateAgencyCommand command =
        new CreateAgencyCommand(
            "Tour Perú", "desc", "12345678901", "info@tourperu.com", "999888777", 1L);
    when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
    when(agencyRepository.existsByUserId(1L)).thenReturn(false);
    when(agencyRepository.existsByRuc("12345678901")).thenReturn(false);
    when(agencyRepository.existsByContactEmail("info@tourperu.com")).thenReturn(true);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));
  }

  // ── Update ───────────────────────────────────────────

  @Test
  @DisplayName("handle(UpdateAgencyCommand) updates and returns agency when validations pass")
  void handle_updateAgency_validData_returnsUpdatedAgency() {
    // Arrange
    UpdateAgencyCommand command =
        new UpdateAgencyCommand(1L, "New Name", "New desc", "new@email.com", "111222333");
    when(agencyRepository.findById(1L)).thenReturn(Optional.of(existingAgency));
    when(agencyRepository.existsByContactEmailAndIdIsNot("new@email.com", 1L)).thenReturn(false);
    when(agencyRepository.save(existingAgency)).thenReturn(existingAgency);

    // Act
    Agency result = agencyCommandService.handle(command);

    // Assert
    assertNotNull(result);
    verify(agencyRepository).save(existingAgency);
  }

  @Test
  @DisplayName("handle(UpdateAgencyCommand) throws when agency does not exist")
  void handle_updateAgency_notFound_throwsIllegalArgument() {
    // Arrange
    UpdateAgencyCommand command =
        new UpdateAgencyCommand(999L, "Name", "desc", "e@mail.com", "123456789");
    when(agencyRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));
  }

  @Test
  @DisplayName("handle(UpdateAgencyCommand) throws when email is taken by another agency")
  void handle_updateAgency_emailTakenByOther_throwsIllegalArgument() {
    // Arrange
    UpdateAgencyCommand command = new UpdateAgencyCommand(1L, null, null, "taken@email.com", null);
    when(agencyRepository.findById(1L)).thenReturn(Optional.of(existingAgency));
    when(agencyRepository.existsByContactEmailAndIdIsNot("taken@email.com", 1L)).thenReturn(true);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));
  }

  // ── Delete ───────────────────────────────────────────

  @Test
  @DisplayName("handle(DeleteAgencyCommand) deletes agency when it exists")
  void handle_deleteAgency_exists_deletesSuccessfully() {
    // Arrange
    DeleteAgencyCommand command = new DeleteAgencyCommand(1L);
    when(agencyRepository.existsById(1L)).thenReturn(true);

    // Act
    assertDoesNotThrow(() -> agencyCommandService.handle(command));

    // Assert
    verify(agencyRepository).deleteById(1L);
  }

  @Test
  @DisplayName("handle(DeleteAgencyCommand) throws when agency does not exist")
  void handle_deleteAgency_notFound_throwsIllegalArgument() {
    // Arrange
    DeleteAgencyCommand command = new DeleteAgencyCommand(999L);
    when(agencyRepository.existsById(999L)).thenReturn(false);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));

    verify(agencyRepository, never()).deleteById(any());
  }
}
