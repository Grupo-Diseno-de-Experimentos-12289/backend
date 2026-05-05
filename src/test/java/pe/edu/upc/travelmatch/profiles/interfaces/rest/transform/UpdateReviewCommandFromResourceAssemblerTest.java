package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.UpdateReviewResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateReviewCommandFromResourceAssemblerTest {

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        var resource = new UpdateReviewResource(4, "Updated comment");

        var command = UpdateReviewCommandFromResourceAssembler.toCommandFromResource(30L, resource);

        assertEquals(30L, command.reviewId());
        assertEquals(4, command.rating().rating());
        assertEquals("Updated comment", command.comment());
    }
}
