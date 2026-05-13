package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
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
import pe.edu.upc.travelmatch.experiences.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("ExperienceCommandServiceImpl Tests")
public class ExperienceCommandServiceImplTest {

  @Mock private ExperienceRepository experienceRepository;

  @Mock private CategoryRepository categoryRepository;

  @Mock private ExternalIamService externalIamService;

  @InjectMocks private ExperienceCommandServiceImpl experienceCommandService;

  private Experience existingExperience;
  private Category existingCategory;

  @BeforeEach
  void setUp() {
    existingCategory = new Category(Categories.CULTURA);
    existingExperience =
        new Experience(
            "Visita a Machu Picchu",
            "Experiencia inolvidable en la ciudadela Inca",
            new AgencyId(1L),
            existingCategory,
            new DestinationId(100L),
            "4 horas",
            "Plaza de Armas");
  }

  // ==========================================
  // TESTS PARA CREATE EXPERIENCE
  // ==========================================

  @Test
  @DisplayName("Debe crear una experiencia exitosamente cuando las validaciones pasan")
  void handle_createExperience_allValid_experienceIsSaved() {
    // --- Arrange (Preparar) ---
    final CreateExperienceCommand command =
        new CreateExperienceCommand(
            "Visita a Machu Picchu",
            "Experiencia inolvidable en la ciudadela Inca",
            1L,
            "CULTURA",
            new DestinationId(100L),
            "4 horas",
            "Plaza de Armas");
    doNothing().when(externalIamService).validateAgencyExists(1L);
    when(categoryRepository.findByName(Categories.CULTURA))
        .thenReturn(Optional.of(existingCategory));

    // Capturador de datos para replicar el estilo de Agencies
    ArgumentCaptor<Experience> experienceCaptor = ArgumentCaptor.forClass(Experience.class);
    when(experienceRepository.save(experienceCaptor.capture()))
        .thenAnswer(inv -> inv.getArgument(0));

    // --- Act (Ejecutar) ---
    experienceCommandService.handle(command);

    // --- Assert (Verificar) ---
    verify(externalIamService, times(1)).validateAgencyExists(1L);
    verify(experienceRepository, times(1)).save(any(Experience.class));
    Experience savedExperience = experienceCaptor.getValue();
    assertEquals("Visita a Machu Picchu", savedExperience.getTitle());
  }

  @Test
  @DisplayName("Debe lanzar excepción al crear si la categoría de la experiencia no existe")
  void handle_createExperience_categoryNotFound_throwsException() {
    // --- Arrange (Preparar) ---
    CreateExperienceCommand command =
        new CreateExperienceCommand(
            "Visita a Machu Picchu",
            "Experiencia inolvidable en la ciudadela Inca",
            1L,
            "CULTURA",
            new DestinationId(100L),
            "4 horas",
            "Plaza de Armas");
    doNothing().when(externalIamService).validateAgencyExists(1L);
    when(categoryRepository.findByName(Categories.CULTURA)).thenReturn(Optional.empty());

    // --- Act & Assert (Ejecutar y Verificar) ---
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> experienceCommandService.handle(command));

    assertTrue(exception.getMessage().contains("Category not found with name"));
    verify(experienceRepository, never()).save(any());
  }

  // ==========================================
  // TESTS PARA UPDATE EXPERIENCE
  // ==========================================

  @Test
  @DisplayName("Debe actualizar la experiencia cuando los datos son válidos")
  void handle_updateExperience_validData_returnsOptionalUpdatedExperience() {
    // --- Arrange (Preparar) ---
    final UpdateExperienceCommand command =
        new UpdateExperienceCommand(
            1L,
            "Titulo Nuevo",
            "Nueva descripcion",
            "CULTURA",
            new DestinationId(100L),
            "5 horas",
            "Plaza");
    when(experienceRepository.findById(1L)).thenReturn(Optional.of(existingExperience));
    when(categoryRepository.findByName(Categories.CULTURA))
        .thenReturn(Optional.of(existingCategory));

    when(experienceRepository.save(any(Experience.class))).thenReturn(existingExperience);

    // --- Act (Ejecutar) ---
    experienceCommandService.updateExperience(command);

    // --- Assert (Verificar) ---
    assertEquals("Titulo Nuevo", existingExperience.getTitle());
    verify(experienceRepository, times(1)).save(existingExperience);
  }

  @Test
  @DisplayName("Debe lanzar excepción al actualizar si la experiencia no existe")
  void handle_updateExperience_notFound_throwsException() {
    // --- Arrange (Preparar) ---
    UpdateExperienceCommand command =
        new UpdateExperienceCommand(
            999L, "Title", "desc", "CULTURA", new DestinationId(100L), "2 horas", "Local");
    when(experienceRepository.findById(999L)).thenReturn(Optional.empty());

    // --- Act & Assert (Ejecutar y Verificar) ---
    assertThrows(RuntimeException.class, () -> experienceCommandService.updateExperience(command));
    verify(experienceRepository, never()).save(any());
  }
}
