package pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.ExperiencesContextFacade;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalExperienceServiceTest {

    @Mock
    private ExperiencesContextFacade experiencesContextFacade;

    @InjectMocks
    private ExternalExperienceService externalExperienceService;

    @Test
    void testExistsExperienceById_True() {
        // Arrange
        ExperienceId experienceId = new ExperienceId(1L);
        when(experiencesContextFacade.existsExperienceById(experienceId.experienceId())).thenReturn(true);

        // Act
        boolean result = externalExperienceService.existsExperienceById(experienceId);

        // Assert
        assertTrue(result);
        verify(experiencesContextFacade, times(1)).existsExperienceById(experienceId.experienceId());
    }

    @Test
    void testExistsExperienceById_False() {
        // Arrange
        ExperienceId experienceId = new ExperienceId(1L);
        when(experiencesContextFacade.existsExperienceById(experienceId.experienceId())).thenReturn(false);

        // Act
        boolean result = externalExperienceService.existsExperienceById(experienceId);

        // Assert
        assertFalse(result);
        verify(experiencesContextFacade, times(1)).existsExperienceById(experienceId.experienceId());
    }
}
