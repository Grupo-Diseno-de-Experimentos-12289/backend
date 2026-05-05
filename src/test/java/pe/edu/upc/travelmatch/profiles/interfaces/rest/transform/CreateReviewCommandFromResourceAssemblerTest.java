package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateReviewResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateReviewCommandFromResourceAssemblerTest {

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        var resource = new CreateReviewResource(1L, 50L, 5, "Great experience");

        var command = CreateReviewCommandFromResourceAssembler.toCommandFromResource(resource);

        assertEquals(1L, command.userId().userId());
        assertEquals(50L, command.experienceId().experienceId());
        assertEquals(5, command.rating().rating());
        assertEquals("Great experience", command.comment());
    }
}
