package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.ExperienceResource;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.UpdateExperienceResource;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateExperienceCommandFromResourceAssemblerTest {

    @Test
    @DisplayName("toCommandFromResource should map ExperienceResource to UpdateExperienceCommand (AAA)")
    void toCommandFromResource_ShouldMap() {
        // Arrange
        Category cat = mock(Category.class);
        var resource = new UpdateExperienceResource( "title", "description",
                "category1", 3L, "3days", "Place1");

        var newExperience = 2L;

        // Act
        UpdateExperienceCommand cmd = UpdateExperienceCommandFromResourceAssembler.toCommandFromResource(resource, newExperience);

        //Assert
        assertNotNull(cmd);
        assertEquals(newExperience, cmd.id());
        assertEquals("title", cmd.title());
        assertEquals("description", cmd.description());
        assertEquals("category1", cmd.category());
        assertEquals("3days", cmd.duration());
        assertEquals("Place1", cmd.meetingPoint());
    }
}
