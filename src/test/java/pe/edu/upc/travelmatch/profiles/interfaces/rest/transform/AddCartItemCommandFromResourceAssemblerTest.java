package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.AddCartItemResource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddCartItemCommandFromResourceAssemblerTest {

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        var resource = new AddCartItemResource(101L, 2, BigDecimal.valueOf(49.99));

        var command = AddCartItemCommandFromResourceAssembler.toCommandFromResource(1L, resource);

        assertEquals(1L, command.userId().userId());
        assertEquals(101L, command.availabilityId().availabilityId());
        assertEquals(2, command.quantity().value());
    }
}
