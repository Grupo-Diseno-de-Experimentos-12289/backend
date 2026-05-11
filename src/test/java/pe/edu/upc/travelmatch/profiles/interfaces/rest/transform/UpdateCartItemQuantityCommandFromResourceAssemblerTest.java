package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.UpdateCartItemQuantityResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCartItemQuantityCommandFromResourceAssemblerTest {

    @Mock
    private UpdateCartItemQuantityResource resource;

    @Test
    void toCommandFromResourceMapsResourceToCommand() {
        when(resource.availabilityId()).thenReturn(101L);
        when(resource.newQuantity()).thenReturn(5);

        var command = UpdateCartItemQuantityCommandFromResourceAssembler.toCommandFromResource(1L, resource);

        assertEquals(1L, command.userId().userId());
        assertEquals(101L, command.availabilityId().availabilityId());
        assertEquals(5, command.newQuantity().value());
    }
}
