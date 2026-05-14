package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.UpdateExperienceMediaResource;

class UpdateExperienceMediaCommandFromResourceAssemblerTest {

  @Test
  @DisplayName(
      "toCommandFromResource should map ExperienceResource to UpdateExperienceMediaCommand (AAA)")
  void toCommandFromResource_ShouldMap() {
    // Arrange
    var resource = new UpdateExperienceMediaResource("http://example.com/media.jpg", "image/jpeg");
    // Act
    UpdateExperienceMediaCommand cmd =
        UpdateExperienceMediaCommandFromResourceAssembler.toCommandFromResource(1L, resource);
    // Assert
    assertNotNull(cmd);
    assertEquals(1L, cmd.id());
    assertEquals("http://example.com/media.jpg", cmd.mediaUrl());
    assertEquals("image/jpeg", cmd.caption());
  }
}
