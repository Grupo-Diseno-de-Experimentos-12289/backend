package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.UpdateCartItemQuantityResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateCartItemQuantityCommandFromResourceAssemblerTest {

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        var resource = new UpdateCartItemQuantityResource(101L, 5);

        var command = UpdateCartItemQuantityCommandFromResourceAssembler.toCommandFromResource(1L, resource);

        assertEquals(1L, command.userId().userId());
        assertEquals(101L, command.availabilityId().availabilityId());
        assertEquals(5, command.newQuantity().value());
    }
}
