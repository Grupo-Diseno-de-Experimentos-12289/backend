package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateFavoriteResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateFavoriteCommandFromResourceAssemblerTest {

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        var resource = new CreateFavoriteResource(1L, 50L);

        var command = CreateFavoriteCommandFromResourceAssembler.toCommandFromResource(resource);

        assertEquals(1L, command.userId().userId());
        assertEquals(50L, command.experienceId().experienceId());
    }
}
