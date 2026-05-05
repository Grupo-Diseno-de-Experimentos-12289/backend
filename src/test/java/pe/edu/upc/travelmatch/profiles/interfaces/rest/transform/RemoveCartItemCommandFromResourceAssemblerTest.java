package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.RemoveCartItemResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RemoveCartItemCommandFromResourceAssemblerTest {

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        var resource = new RemoveCartItemResource(101L);

        var command = RemoveCartItemCommandFromResourceAssembler.toCommandFromResource(1L, resource);

        assertEquals(1L, command.userId().userId());
        assertEquals(101L, command.availabilityId().availabilityId());
    }
}
