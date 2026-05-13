package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityTicketTypeCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityCommandService;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityQueryService;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityTicketTypeCommandService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.AvailabilityResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateAvailabilityResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateAvailabilityTicketTypeResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.UpdateAvailabilityResource;

@ExtendWith(MockitoExtension.class)
class AvailabilitiesControllerTest {

  @Mock private AvailabilityCommandService availabilityCommandService;

  @Mock private AvailabilityQueryService availabilityQueryService;

  @Mock private AvailabilityTicketTypeCommandService availabilityTicketTypeCommandService;

  @Mock private ExperienceRepository experienceRepository;

  @Mock private AvailabilityRepository availabilityRepository;

  @InjectMocks private AvailabilitiesController availabilitiesController;

  private Experience experience;
  private Availability availability;

  @BeforeEach
  void setUp() {
    experience = mock(Experience.class);
    availability = mock(Availability.class);
  }

  @Test
  void testCreateAvailability_Ok() {
    // Arrange
    Long experienceId = 1L;
    CreateAvailabilityResource resource =
        new CreateAvailabilityResource(
            2L, LocalDateTime.now(), LocalDateTime.now().plusHours(2), 20);

    when(experienceRepository.findById(experienceId)).thenReturn(Optional.of(experience));
    when(availabilityCommandService.handle(any(CreateAvailabilityCommand.class))).thenReturn(100L);

    // Act
    ResponseEntity<Long> response =
        availabilitiesController.createAvailability(experienceId, resource);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(100L, response.getBody());
  }

  @Test
  void testCreateAvailability_ExperienceNotFound() {
    // Arrange
    Long experienceId = 1L;
    CreateAvailabilityResource resource =
        new CreateAvailabilityResource(
            2L, LocalDateTime.now(), LocalDateTime.now().plusHours(2), 20);

    when(experienceRepository.findById(experienceId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        RuntimeException.class,
        () -> availabilitiesController.createAvailability(experienceId, resource));
  }

  @Test
  void testUpdateAvailability_NoContent() {
    // Arrange
    Long availabilityId = 100L;
    UpdateAvailabilityResource resource =
        new UpdateAvailabilityResource(LocalDateTime.now(), LocalDateTime.now().plusHours(3), 30);

    doNothing()
        .when(availabilityCommandService)
        .updateAvailability(any(UpdateAvailabilityCommand.class));

    // Act
    ResponseEntity<Void> response =
        availabilitiesController.updateAvailability(availabilityId, resource);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(availabilityCommandService, times(1))
        .updateAvailability(any(UpdateAvailabilityCommand.class));
  }

  @Test
  void testDeleteAvailability_NoContent() {
    // Arrange
    Long availabilityId = 100L;
    doNothing().when(availabilityCommandService).deleteAvailability(availabilityId);

    // Act
    ResponseEntity<Void> response = availabilitiesController.deleteAvailability(availabilityId);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(availabilityCommandService, times(1)).deleteAvailability(availabilityId);
  }

  @Test
  void testGetAllAvailabilities_Ok() {
    // Arrange
    when(availability.getExperience()).thenReturn(experience);
    when(experience.getId()).thenReturn(1L);
    when(availabilityQueryService.getAllAvailabilities()).thenReturn(List.of(availability));

    // Act
    ResponseEntity<List<AvailabilityResource>> response =
        availabilitiesController.getAllAvailabilities();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void testCreateTicketTypeForAvailability_Ok() {
    // Arrange
    Long availabilityId = 100L;
    CreateAvailabilityTicketTypeResource resource =
        new CreateAvailabilityTicketTypeResource(1L, 2L, "type1", BigDecimal.valueOf(50.0), 10);

    when(availabilityRepository.findById(availabilityId)).thenReturn(Optional.of(availability));
    when(availabilityTicketTypeCommandService.handle(
            any(CreateAvailabilityTicketTypeCommand.class)))
        .thenReturn(500L);

    // Act
    ResponseEntity<Long> response =
        availabilitiesController.createTicketTypeForAvailability(availabilityId, resource);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(500L, response.getBody());
  }

  @Test
  void testCreateTicketTypeForAvailability_NotFound() {
    // Arrange
    Long availabilityId = 100L;
    CreateAvailabilityTicketTypeResource resource =
        new CreateAvailabilityTicketTypeResource(1L, 2L, "type1", BigDecimal.valueOf(50.0), 10);

    when(availabilityRepository.findById(availabilityId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        RuntimeException.class,
        () -> availabilitiesController.createTicketTypeForAvailability(availabilityId, resource));
  }
}
