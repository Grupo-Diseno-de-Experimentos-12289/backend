package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceMediaCommandService;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceMediaQueryService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateExperienceMediaResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.ExperienceMediaResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.UpdateExperienceMediaResource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceMediaControllerTest {

  @Mock private ExperienceMediaCommandService commandService;

  @Mock private ExperienceMediaQueryService queryService;

  @Mock private ExperienceRepository experienceRepository;

  @InjectMocks private ExperienceMediaController experienceMediaController;

  private Experience experience;
  private ExperienceMedia experienceMedia;

  @BeforeEach
  void setUp() {
    // Usamos una instancia real de Experience para evitar problemas con los Assemblers
    experience = new Experience();
    experienceMedia = new ExperienceMedia(experience, "http://media.com/image.jpg", "Caption");
  }

  @Test
  void testCreate_Ok() {
    // Arrange
    Long experienceId = 1L;
    CreateExperienceMediaResource resource =
        new CreateExperienceMediaResource(1L, "url", "caption");

    when(experienceRepository.findById(experienceId)).thenReturn(Optional.of(experience));
    when(commandService.handle(any(CreateExperienceMediaCommand.class))).thenReturn(100L);

    // Act
    ResponseEntity<Long> response = experienceMediaController.create(experienceId, resource);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(100L, response.getBody());
  }

  @Test
  void testCreate_ExperienceNotFound() {
    // Arrange
    Long experienceId = 1L;
    CreateExperienceMediaResource resource =
        new CreateExperienceMediaResource(2L, "url", "caption");

    when(experienceRepository.findById(experienceId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        RuntimeException.class, () -> experienceMediaController.create(experienceId, resource));
  }

  @Test
  void testUpdate_Ok() {
    // Arrange
    Long mediaId = 100L;
    UpdateExperienceMediaResource resource =
        new UpdateExperienceMediaResource("new-url", "new-caption");

    when(commandService.handle(any(UpdateExperienceMediaCommand.class)))
        .thenReturn(Optional.of(experienceMedia));

    // Act
    ResponseEntity<ExperienceMediaResource> response =
        experienceMediaController.update(mediaId, resource);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Caption", response.getBody().caption());
  }

  @Test
  void testUpdate_NotFound() {
    // Arrange
    Long mediaId = 100L;
    UpdateExperienceMediaResource resource = new UpdateExperienceMediaResource("url", "caption");

    when(commandService.handle(any(UpdateExperienceMediaCommand.class)))
        .thenReturn(Optional.empty());

    // Act
    ResponseEntity<ExperienceMediaResource> response =
        experienceMediaController.update(mediaId, resource);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testGetAll_Ok() {
    // Arrange
    when(queryService.getAll()).thenReturn(List.of(experienceMedia));

    // Act
    ResponseEntity<List<ExperienceMediaResource>> response = experienceMediaController.getAll();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void testGetByExperience_Ok() {
    // Arrange
    Long experienceId = 1L;
    when(queryService.findByExperienceId(experienceId)).thenReturn(List.of(experienceMedia));

    // Act
    ResponseEntity<List<ExperienceMediaResource>> response =
        experienceMediaController.getByExperience(experienceId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void testDelete_NoContent() {
    // Arrange
    Long mediaId = 100L;
    doNothing().when(commandService).deleteById(mediaId);

    // Act
    ResponseEntity<Void> response = experienceMediaController.delete(mediaId);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(commandService, times(1)).deleteById(mediaId);
  }
}
