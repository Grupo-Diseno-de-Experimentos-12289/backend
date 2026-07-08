package pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.ExperiencesContextFacade;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.dto.ExperienceSummary;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ExternalExperienceServiceTest {

  @Mock private ExperiencesContextFacade experiencesContextFacade;

  @InjectMocks private ExternalExperienceService externalExperienceService;

  @Test
  void testExistsExperienceById_True() {
    // Arrange
    ExperienceId experienceId = new ExperienceId(1L);
    when(experiencesContextFacade.existsExperienceById(experienceId.experienceId()))
        .thenReturn(true);

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
    when(experiencesContextFacade.existsExperienceById(experienceId.experienceId()))
        .thenReturn(false);

    // Act
    boolean result = externalExperienceService.existsExperienceById(experienceId);

    // Assert
    assertFalse(result);
    verify(experiencesContextFacade, times(1)).existsExperienceById(experienceId.experienceId());
  }
  @Test
  void testFetchExperiencesByDestinationAndCategories_DelegatesToFacade() {
    // Arrange
    Long destinationId = 1L;
    List<String> categories = List.of("CULTURA");
    var summary = new ExperienceSummary(10L, "Tour Centro", "CULTURA", "Plaza Mayor", "3h", 1L);
    when(experiencesContextFacade.fetchExperiencesByDestinationAndCategories(
            destinationId, categories))
            .thenReturn(List.of(summary));

    // Act
    List<ExperienceSummary> result =
            externalExperienceService.fetchExperiencesByDestinationAndCategories(
                    destinationId, categories);

    // Assert
    assertEquals(1, result.size());
    assertEquals(10L, result.get(0).experienceId());
    verify(experiencesContextFacade, times(1))
            .fetchExperiencesByDestinationAndCategories(destinationId, categories);
  }
}
