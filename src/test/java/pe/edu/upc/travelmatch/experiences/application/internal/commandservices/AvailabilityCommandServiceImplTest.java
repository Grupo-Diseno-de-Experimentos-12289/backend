package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;

@ExtendWith(MockitoExtension.class)
class AvailabilityCommandServiceImplTest {

  @Mock private AvailabilityRepository availabilityRepository;

  @Mock private ExperienceRepository experienceRepository;

  @InjectMocks private AvailabilityCommandServiceImpl availabilityCommandService;

  private Experience experience;
  private Availability availability;

  @BeforeEach
  void setUp() {
    experience = new Experience();

    availability =
        new Availability(
            experience, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 20);
  }

  @Test
  void handle_crearDisponibilidadConExito() {
    // Arrange
    CreateAvailabilityCommand command =
        new CreateAvailabilityCommand(
            experience, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 20);

    when(experienceRepository.findById(any())).thenReturn(Optional.of(experience));
    when(availabilityRepository.save(any(Availability.class))).thenReturn(availability);

    // Act
    Long availabilityId = availabilityCommandService.handle(command);

    // Assertt
    verify(experienceRepository, times(1)).findById(any());
    verify(availabilityRepository, times(1)).save(any(Availability.class));
    assertNull(
        availabilityId); // Depende del estado autogenerado del Id, en pruebas simples es nulo si no
    // se setea a mano
  }

  @Test
  void handle_cuandoExperienciaNoExisteLanzaExcepcion() {
    // Arrange
    CreateAvailabilityCommand command =
        new CreateAvailabilityCommand(
            experience, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 20);

    when(experienceRepository.findById(any())).thenReturn(Optional.empty());

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> availabilityCommandService.handle(command));

    assertTrue(exception.getMessage().contains("does not exist"));
    verify(availabilityRepository, never()).save(any(Availability.class));
  }

  @Test
  void updateAvailability_actualizaConExito() {
    // Arrange
    UpdateAvailabilityCommand command =
        new UpdateAvailabilityCommand(
            1L, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), 30);

    when(availabilityRepository.findById(1L)).thenReturn(Optional.of(availability));

    // Act
    availabilityCommandService.updateAvailability(command);

    // Assert
    assertEquals(30, availability.getCapacity());
    verify(availabilityRepository, times(1)).save(availability);
  }

  @Test
  void deleteAvailability_marcaComoEliminadoConExito() {
    // Arrange
    when(availabilityRepository.findById(1L)).thenReturn(Optional.of(availability));

    // Act
    availabilityCommandService.deleteAvailability(1L);

    // Assert
    assertNotNull(availability.getDeletedAt());
    verify(availabilityRepository, times(1)).save(availability);
  }
}
