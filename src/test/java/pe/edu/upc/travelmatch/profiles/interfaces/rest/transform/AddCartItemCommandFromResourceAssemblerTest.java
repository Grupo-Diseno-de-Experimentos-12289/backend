package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.AddCartItemResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddCartItemCommandFromResourceAssemblerTest {

    @Mock
    private AddCartItemResource resource;

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        when(resource.availabilityId()).thenReturn(101L);
        when(resource.quantity()).thenReturn(2);

        var command = AddCartItemCommandFromResourceAssembler.toCommandFromResource(1L, resource);

        assertEquals(1L, command.userId().userId());
        assertEquals(101L, command.availabilityId().availabilityId());
        assertEquals(2, command.quantity().value());
    }
}
