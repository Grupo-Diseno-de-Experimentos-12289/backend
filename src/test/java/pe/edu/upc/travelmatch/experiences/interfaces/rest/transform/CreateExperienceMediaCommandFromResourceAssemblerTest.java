package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateExperienceMediaResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateExperienceMediaCommandFromResourceAssemblerTest {

  @Test
  @DisplayName(
      "toCommand should map CreateExperienceMediaResource to CreateExperienceMediaCommand (AAA)")
  void toCommandFromResource_ShouldMap() {
    // Arrange
    Experience exp = mock(Experience.class);
    when(exp.getId()).thenReturn(1L);
    var resource =
        new CreateExperienceMediaResource(1L, "http://example.com/media.jpg", "image/jpeg");
    // Act

    CreateExperienceMediaCommand expm =
        CreateExperienceMediaCommandFromResourceAssembler.toCommandFromResource(resource, exp);

    // Assert

    assertNotNull(expm);
    assertEquals(1L, expm.experience().getId());
    assertEquals(1L, exp.getId());
    assertEquals("http://example.com/media.jpg", expm.mediaUrl());
    assertEquals("image/jpeg", expm.caption());
  }
}
