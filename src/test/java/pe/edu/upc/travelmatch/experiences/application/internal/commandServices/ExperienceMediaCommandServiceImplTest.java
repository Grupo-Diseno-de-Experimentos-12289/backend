package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceMediaRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
@DisplayName("ExperienceMediaCommandServiceImpl Tests")
public class ExperienceMediaCommandServiceImplTest {

    @Mock
    private ExperienceMediaRepository mediaRepository;

    @Mock
    private ExperienceRepository experienceRepository;

    @InjectMocks
    private ExperienceMediaCommandServiceImpl mediaCommandService;

    private Experience existingExperience;
    private ExperienceMedia existingMedia;

    @BeforeEach
    void setUp() {
        existingExperience = new Experience(
                "Visita a Machu Picchu",
                "Experiencia inolvidable en la ciudadela Inca",
                new AgencyId(1L),
                new Category(Categories.CULTURA),
                new DestinationId(100L),
                "4 horas",
                "Plaza de Armas"
        ) {
            @Override
            public Long getId() {
                return 1L;
            }
        };

        existingMedia = new ExperienceMedia(
                existingExperience,
                "http://image.url",
                "Beautiful landscape"
        );
    }

    // ==========================================
    // TESTS PARA CREATE MEDIA
    // ==========================================

    @Test
    @DisplayName("Debe crear una multimedia de experiencia exitosamente cuando la experiencia existe")
    void handle_createMedia_experienceExists_mediaIsSaved() {
        // --- Arrange (Preparar) ---
        CreateExperienceMediaCommand command = new CreateExperienceMediaCommand(
                existingExperience,
                "http://new.image.url",
                "Awesome view"
        );

        when(experienceRepository.findById(existingExperience.getId())).thenReturn(Optional.of(existingExperience));

        ArgumentCaptor<ExperienceMedia> mediaCaptor = ArgumentCaptor.forClass(ExperienceMedia.class);
        when(mediaRepository.save(mediaCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        // --- Act (Ejecutar) ---
        mediaCommandService.handle(command);

        // --- Assert (Verificar) ---
        verify(experienceRepository, times(1)).findById(any());
        verify(mediaRepository, times(1)).save(any(ExperienceMedia.class));

        ExperienceMedia savedMedia = mediaCaptor.getValue();
        assertEquals("http://new.image.url", savedMedia.getMediaUrl());
        assertEquals("Awesome view", savedMedia.getCaption());
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear multimedia si la experiencia no existe")
    void handle_createMedia_experienceNotFound_throwsException() {
        // --- Arrange (Preparar) ---
        CreateExperienceMediaCommand command = new CreateExperienceMediaCommand(
                existingExperience,
                "http://new.image.url",
                "Awesome view"
        );
        when(experienceRepository.findById(any())).thenReturn(Optional.empty());

        // --- Act & Assert (Ejecutar y Verificar) ---
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> mediaCommandService.handle(command));

        assertTrue(exception.getMessage().contains("does not exist"));
        verify(mediaRepository, never()).save(any());
    }

    // ==========================================
    // TESTS PARA UPDATE MEDIA
    // ==========================================

    @Test
    @DisplayName("Debe actualizar la multimedia exitosamente cuando los datos son válidos")
    void handle_updateMedia_validData_returnsUpdatedMedia() {
        // --- Arrange (Preparar) ---
        UpdateExperienceMediaCommand command = new UpdateExperienceMediaCommand(
                1L,
                "http://updated.image.url",
                "Updated caption"
        );
        when(mediaRepository.findById(1L)).thenReturn(Optional.of(existingMedia));
        when(mediaRepository.save(any(ExperienceMedia.class))).thenReturn(existingMedia);

        // --- Act (Ejecutar) ---
        Optional<ExperienceMedia> result = mediaCommandService.handle(command);

        // --- Assert (Verificar) ---
        assertTrue(result.isPresent());
        assertEquals("http://updated.image.url", existingMedia.getMediaUrl());
        assertEquals("Updated caption", existingMedia.getCaption());
        verify(mediaRepository, times(1)).save(existingMedia);
    }

    // ==========================================
    // TESTS PARA DELETE MEDIA
    // ==========================================

    @Test
    @DisplayName("Debe eliminar lógicamente la multimedia si existe")
    void deleteById_mediaExists_marksAsDeleted() {
        // --- Arrange (Preparar) ---
        when(mediaRepository.findById(1L)).thenReturn(Optional.of(existingMedia));

        // --- Act (Ejecutar) ---
        assertDoesNotThrow(() -> mediaCommandService.deleteById(1L));

        // --- Assert (Verificar) ---
        assertNotNull(existingMedia.getDeletedAt());
        verify(mediaRepository, times(1)).findById(1L);
    }
}
