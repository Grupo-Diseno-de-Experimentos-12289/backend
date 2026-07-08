package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.UpdateExperienceResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UpdateExperienceCommandFromResourceAssemblerTest {

  @Test
  @DisplayName(
      "toCommandFromResource should map ExperienceResource to UpdateExperienceCommand (AAA)")
  void toCommandFromResource_ShouldMap() {
    // Arrange
    var resource =
        new UpdateExperienceResource(
            "title",
            "description",
            "category1",
            3L,
            "3days",
            "Place1",
            "MODERATE",
            "Partial refund up to 5 days before.");

    var newExperience = 2L;

    // Act
    UpdateExperienceCommand cmd =
        UpdateExperienceCommandFromResourceAssembler.toCommandFromResource(resource, newExperience);

    // Assert
    assertNotNull(cmd);
    assertEquals(newExperience, cmd.id());
    assertEquals("title", cmd.title());
    assertEquals("description", cmd.description());
    assertEquals("category1", cmd.category());
    assertEquals("3days", cmd.duration());
    assertEquals("Place1", cmd.meetingPoint());
    assertEquals(
        pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType
            .MODERATE,
        cmd.cancellationPolicyType());
    assertEquals("Partial refund up to 5 days before.", cmd.cancellationPolicyDescription());
  }

  @Test
  @DisplayName("toCommandFromResource should map null policy type when none is provided")
  void toCommandFromResource_ShouldMapNullPolicyType() {
    // Arrange
    var resource =
        new UpdateExperienceResource("title", "description", "category1", 3L, "3days", "Place1", null, null);

    // Act
    UpdateExperienceCommand cmd =
        UpdateExperienceCommandFromResourceAssembler.toCommandFromResource(resource, 2L);

    // Assert
    assertEquals(null, cmd.cancellationPolicyType());
  }
}
