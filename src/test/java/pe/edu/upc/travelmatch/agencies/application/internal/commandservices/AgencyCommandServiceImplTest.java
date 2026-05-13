package pe.edu.upc.travelmatch.agencies.application.internal.commandservices;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
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
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.valueobjects.AgencyName;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.interfaces.acl.AgenciesContextFacade;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("AgencyCommandServiceImpl Tests")
public class AgencyCommandServiceImplTest {
  @Mock private AgencyRepository agencyRepository;

  @Mock private AgenciesContextFacade agenciesContextFacade;

  @InjectMocks private AgencyCommandServiceImpl agencyCommandService;

  private Agency existingAgency;

  @BeforeEach
  void setUp() {
    existingAgency =
        new Agency(
            new AgencyName("Tour Perú"),
            "Wonderful tours",
            "12345678901",
            "info@tourperu.com",
            "999888777",
            1L);
  }

  // ==========================================
  // TESTS PARA CREATE AGENCY
  // ==========================================

  @Test
  @DisplayName("Debe crear una agencia exitosamente cuando todas las validaciones pasan")
  void handle_createAgency_allValid_agencyIsSaved() {
    // --- Arrange (Preparar) ---
    CreateAgencyCommand command =
        new CreateAgencyCommand(
            "Tour Perú", "Great tours", "12345678901", "info@tourperu.com", "999888777", 1L);
    when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
    when(agencyRepository.existsByUserId(1L)).thenReturn(false);
    when(agencyRepository.existsByRuc("12345678901")).thenReturn(false);
    when(agencyRepository.existsByContactEmail("info@tourperu.com")).thenReturn(false);

    // Usamos un capturador para verificar los datos exactos que se guardan
    ArgumentCaptor<Agency> agencyCaptor = ArgumentCaptor.forClass(Agency.class);
    when(agencyRepository.save(agencyCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

    // --- Act (Ejecutar) ---
    Long resultId = agencyCommandService.handle(command);

    // --- Assert (Verificar) ---
    verify(agencyRepository, times(1)).save(any(Agency.class));
    Agency savedAgency = agencyCaptor.getValue();
    assertEquals("Tour Perú", savedAgency.getName().getName());
    assertEquals("12345678901", savedAgency.getRuc());
  }

  @Test
  @DisplayName("Debe lanzar excepción al crear si el usuario no existe en IAM")
  void handle_createAgency_userNotFound_throwsException() {
    // --- Arrange (Preparar) ---
    CreateAgencyCommand command =
        new CreateAgencyCommand(
            "Tour Perú", "desc", "12345678901", "info@tourperu.com", "999888777", 99L);
    when(agenciesContextFacade.existsUserById(99L)).thenReturn(false);

    // --- Act & Assert (Ejecutar y Verificar) ---
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));

    assertTrue(exception.getMessage().contains("does not exist in IAM"));
    verify(agencyRepository, never()).save(any());
  }

  @Test
  @DisplayName("Debe lanzar excepción al crear si el usuario ya tiene una agencia")
  void handle_createAgency_duplicateUserId_throwsException() {
    // --- Arrange (Preparar) ---
    CreateAgencyCommand command =
        new CreateAgencyCommand(
            "Tour Perú", "desc", "12345678901", "info@tourperu.com", "999888777", 1L);
    when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
    when(agencyRepository.existsByUserId(1L)).thenReturn(true);

    // --- Act & Assert (Ejecutar y Verificar) ---
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));
    verify(agencyRepository, never()).save(any());
  }

  @Test
  @DisplayName("Debe lanzar excepción al crear si el RUC ya está registrado")
  void handle_createAgency_duplicateRuc_throwsException() {
    // --- Arrange (Preparar) ---
    final CreateAgencyCommand command =
        new CreateAgencyCommand(
            "Tour Perú", "desc", "12345678901", "info@tourperu.com", "999888777", 1L);
    when(agenciesContextFacade.existsUserById(1L)).thenReturn(true);
    when(agencyRepository.existsByUserId(1L)).thenReturn(false);
    when(agencyRepository.existsByRuc("12345678901")).thenReturn(true);

    // --- Act & Assert (Ejecutar y Verificar) ---
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));
    verify(agencyRepository, never()).save(any());
  }

  // ==========================================
  // TESTS PARA UPDATE AGENCY
  // ==========================================

  @Test
  @DisplayName("Debe actualizar la agencia cuando los datos son válidos")
  void handle_updateAgency_validData_returnsUpdatedAgency() {
    // --- Arrange (Preparar) ---
    UpdateAgencyCommand command =
        new UpdateAgencyCommand(1L, "Nuevo Nombre", "Nueva desc", "nuevo@email.com", "111222333");
    when(agencyRepository.findById(1L)).thenReturn(Optional.of(existingAgency));
    // Simulamos que el nuevo email NO le pertenece a OTRA agencia
    when(agencyRepository.existsByContactEmailAndIdIsNot("nuevo@email.com", 1L)).thenReturn(false);
    when(agencyRepository.save(existingAgency)).thenReturn(existingAgency);

    // --- Act (Ejecutar) ---
    Agency result = agencyCommandService.handle(command);

    // --- Assert (Verificar) ---
    assertNotNull(result);
    assertEquals("Nuevo Nombre", result.getName().getName());
    assertEquals("nuevo@email.com", result.getContactEmail());
    verify(agencyRepository, times(1)).save(existingAgency);
  }

  @Test
  @DisplayName("Debe lanzar excepción al actualizar si la agencia no existe")
  void handle_updateAgency_notFound_throwsException() {
    // --- Arrange (Preparar) ---
    UpdateAgencyCommand command =
        new UpdateAgencyCommand(999L, "Name", "desc", "e@mail.com", "123456789");
    when(agencyRepository.findById(999L)).thenReturn(Optional.empty());

    // --- Act & Assert (Ejecutar y Verificar) ---
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));
    verify(agencyRepository, never()).save(any());
  }

  @Test
  @DisplayName("Debe lanzar excepción al actualizar si el email ya lo tiene otra agencia")
  void handle_updateAgency_emailTakenByOther_throwsException() {
    // --- Arrange (Preparar) ---
    UpdateAgencyCommand command = new UpdateAgencyCommand(1L, null, null, "tomado@email.com", null);
    when(agencyRepository.findById(1L)).thenReturn(Optional.of(existingAgency));
    when(agencyRepository.existsByContactEmailAndIdIsNot("tomado@email.com", 1L)).thenReturn(true);

    // --- Act & Assert (Ejecutar y Verificar) ---
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));
    verify(agencyRepository, never()).save(any());
  }

  // ==========================================
  // TESTS PARA DELETE AGENCY
  // ==========================================

  @Test
  @DisplayName("Debe eliminar la agencia correctamente si esta existe")
  void handle_deleteAgency_exists_deletesSuccessfully() {
    // --- Arrange (Preparar) ---
    DeleteAgencyCommand command = new DeleteAgencyCommand(1L);
    when(agencyRepository.existsById(1L)).thenReturn(true);

    // --- Act (Ejecutar) ---
    assertDoesNotThrow(() -> agencyCommandService.handle(command));

    // --- Assert (Verificar) ---
    verify(agencyRepository, times(1)).deleteById(1L);
  }

  @Test
  @DisplayName("Debe lanzar excepción al eliminar si la agencia no existe")
  void handle_deleteAgency_notFound_throwsException() {
    // --- Arrange (Preparar) ---
    DeleteAgencyCommand command = new DeleteAgencyCommand(999L);
    when(agencyRepository.existsById(999L)).thenReturn(false);

    // --- Act & Assert (Ejecutar y Verificar) ---
    assertThrows(IllegalArgumentException.class, () -> agencyCommandService.handle(command));
    verify(agencyRepository, never()).deleteById(any());
  }
}
