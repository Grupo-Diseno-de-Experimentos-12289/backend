package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateExperienceResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateExperienceCommandFromResourceAssemblerTest {

  @Test
  @DisplayName("toCommand should map CreateExperienceResource to CreateExperienceCommand (AAA)")
  void toCommandFromResource_ShouldMap() {
    // Arrange
    var resource =
        new CreateExperienceResource(
            "experience1",
            "Description1",
            "category1",
            1L,
            "50 minutes",
            "meet1",
            "FLEXIBLE",
            "Free cancellation up to 24 hours before.");
    // Act

    CreateExperienceCommand exp =
        CreateExperienceCommandFromResourceAssembler.toCommandFromResource(resource, 1L);
    // Assert
    assertNotNull(exp);
    assertEquals("experience1", exp.title());
    assertEquals("Description1", exp.description());
    assertEquals("category1", exp.category());
    assertEquals(1L, exp.agencyId());
    assertEquals("50 minutes", exp.duration());
    assertEquals("meet1", exp.meetingPoint());
    assertEquals(
        pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType
            .FLEXIBLE,
        exp.cancellationPolicyType());
    assertEquals("Free cancellation up to 24 hours before.", exp.cancellationPolicyDescription());
  }

  @Test
  @DisplayName("toCommandFromResource should default to FLEXIBLE policy when none is provided")
  void toCommandFromResource_ShouldDefaultToFlexiblePolicy() {
    // Arrange
    var resource =
        new CreateExperienceResource(
            "experience1", "Description1", "category1", 1L, "50 minutes", "meet1", null, null);

    // Act
    CreateExperienceCommand exp =
        CreateExperienceCommandFromResourceAssembler.toCommandFromResource(resource, 1L);

    // Assert
    assertEquals(
        pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType
            .FLEXIBLE,
        exp.cancellationPolicyType());
  }
}
