package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateCartResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateCartCommandFromResourceAssemblerTest {

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        var resource = new CreateCartResource(1L);

        var command = CreateCartCommandFromResourceAssembler.toCommandFromResource(resource);

        assertEquals(1L, command.userId().userId());
    }
}
