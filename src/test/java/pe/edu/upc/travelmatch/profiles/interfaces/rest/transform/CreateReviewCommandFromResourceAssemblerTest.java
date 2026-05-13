package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateReviewResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateReviewCommandFromResourceAssemblerTest {

    @Mock
    private CreateReviewResource resource;

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        when(resource.userId()).thenReturn(1L);
        when(resource.experienceId()).thenReturn(50L);
        when(resource.rating()).thenReturn(5);
        when(resource.comment()).thenReturn("Great experience");

        var command = CreateReviewCommandFromResourceAssembler.toCommandFromResource(resource);

        assertEquals(1L, command.userId().userId());
        assertEquals(50L, command.experienceId().experienceId());
        assertEquals(5, command.rating().rating());
        assertEquals("Great experience", command.comment());
    }
}
