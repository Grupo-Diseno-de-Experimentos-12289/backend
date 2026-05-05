package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.UpdateReviewResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateReviewCommandFromResourceAssemblerTest {

    @Mock
    private UpdateReviewResource resource;

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        when(resource.rating()).thenReturn(4);
        when(resource.comment()).thenReturn("Updated comment");

        var command = UpdateReviewCommandFromResourceAssembler.toCommandFromResource(30L, resource);

        assertEquals(30L, command.reviewId());
        assertEquals(4, command.rating().rating());
        assertEquals("Updated comment", command.comment());
    }
}
