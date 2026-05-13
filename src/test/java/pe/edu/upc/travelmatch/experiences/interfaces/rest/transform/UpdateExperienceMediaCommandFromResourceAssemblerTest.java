package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.UpdateExperienceMediaResource;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateExperienceMediaCommandFromResourceAssemblerTest {

    @Test
    @DisplayName("toCommandFromResource should map ExperienceResource to UpdateExperienceMediaCommand (AAA)")
    void toCommandFromResource_ShouldMap(){
        //Arrange
        var resource = new UpdateExperienceMediaResource("http://example.com/media.jpg", "image/jpeg");
        var newExperienceMedia = 1L;
        //Act
        UpdateExperienceMediaCommand cmd = UpdateExperienceMediaCommandFromResourceAssembler.toCommandFromResource(1L,resource);
        //Assert
        assertNotNull(cmd);
        assertEquals(1L, cmd.id());
        assertEquals("http://example.com/media.jpg", cmd.mediaUrl());
        assertEquals("image/jpeg", cmd.caption());
    }
}
