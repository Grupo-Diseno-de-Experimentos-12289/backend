package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllExperiencesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetExperienceByIdQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceCommandService;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceQueryService;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateExperienceResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.ExperienceResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.UpdateExperienceResource;

@ExtendWith(MockitoExtension.class)
class ExperiencesControllerTest {

  @Mock private ExperienceCommandService commandService;

  @Mock private ExperienceQueryService queryService;

  @InjectMocks private ExperiencesController experiencesController;

  private Experience experience;

  @BeforeEach
  void setUp() {
    // Inicializamos con objetos reales para que el Assembler no encuentre nulos
    AgencyId agencyId = new AgencyId(1L);
    DestinationId destinationId = new DestinationId(1L);
    Category category = new Category(Categories.CULTURA);

    experience =
        new Experience(
            "Machu Picchu Full Day",
            "A great experience in Cusco",
            agencyId,
            category,
            destinationId,
            "12h",
            "Main Square");
  }

  @Test
  void testCreateExperience_Created() {
    // Arrange
    Long agencyId = 1L;
    CreateExperienceResource resource =
        new CreateExperienceResource(
            "Machu Picchu Full Day", "Description", "CULTURA", 1L, "12h", "Square");

    when(commandService.handle(any(CreateExperienceCommand.class))).thenReturn(10L);
    when(queryService.handle(any(GetExperienceByIdQuery.class)))
        .thenReturn(Optional.of(experience));

    // Act
    ResponseEntity<ExperienceResource> response =
        experiencesController.createExperience(agencyId, resource);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Machu Picchu Full Day", response.getBody().title());
  }

  @Test
  void testCreateExperience_NotFound() {
    // Arrange
    Long agencyId = 1L;
    CreateExperienceResource resource =
        new CreateExperienceResource("Title", "Desc", "CULTURA", 1L, "1h", "Point");

    when(commandService.handle(any(CreateExperienceCommand.class))).thenReturn(10L);
    when(queryService.handle(any(GetExperienceByIdQuery.class))).thenReturn(Optional.empty());

    // Act
    ResponseEntity<ExperienceResource> response =
        experiencesController.createExperience(agencyId, resource);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testGetAllExperiences_Ok() {
    // Arrange
    when(queryService.handle(any(GetAllExperiencesQuery.class))).thenReturn(List.of(experience));

    // Act
    ResponseEntity<List<ExperienceResource>> response = experiencesController.getAllExperiences();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("Machu Picchu Full Day", response.getBody().get(0).title());
  }

  @Test
  void testUpdateExperience_Ok() {
    // Arrange
    Long experienceId = 10L;
    UpdateExperienceResource resource =
        new UpdateExperienceResource(
            "Updated Title", "Updated Desc", "CULTURA", 1L, "5h", "New Point");

    doNothing().when(commandService).updateExperience(any(UpdateExperienceCommand.class));
    when(queryService.handle(any(GetExperienceByIdQuery.class)))
        .thenReturn(Optional.of(experience));

    // Act
    ResponseEntity<ExperienceResource> response =
        experiencesController.updateExperience(experienceId, resource);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void testUpdateExperience_NotFound() {
    // Arrange
    Long experienceId = 10L;
    UpdateExperienceResource resource =
        new UpdateExperienceResource("Title", "Desc", "CULTURA", 1L, "5h", "Point");

    doNothing().when(commandService).updateExperience(any(UpdateExperienceCommand.class));
    when(queryService.handle(any(GetExperienceByIdQuery.class))).thenReturn(Optional.empty());

    // Act
    ResponseEntity<ExperienceResource> response =
        experiencesController.updateExperience(experienceId, resource);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testDeleteExperience_NoContent() {
    // Arrange
    Long experienceId = 10L;
    doNothing().when(commandService).deleteExperience(experienceId);

    // Act
    ResponseEntity<Void> response = experiencesController.deleteExperience(experienceId);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(commandService, times(1)).deleteExperience(experienceId);
  }

  @Test
  void testGetExperienceById_Ok() {
    // Arrange
    Long experienceId = 10L;
    when(queryService.handle(any(GetExperienceByIdQuery.class)))
        .thenReturn(Optional.of(experience));

    // Act
    ResponseEntity<ExperienceResource> response =
        experiencesController.getExperienceById(experienceId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Machu Picchu Full Day", response.getBody().title());
  }

  @Test
  void testGetExperienceById_NotFound() {
    // Arrange
    Long experienceId = 10L;
    when(queryService.handle(any(GetExperienceByIdQuery.class))).thenReturn(Optional.empty());

    // Act
    ResponseEntity<ExperienceResource> response =
        experiencesController.getExperienceById(experienceId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
