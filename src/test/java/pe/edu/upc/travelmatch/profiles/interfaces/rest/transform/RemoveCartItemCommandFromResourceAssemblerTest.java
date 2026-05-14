package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.RemoveCartItemResource;

@ExtendWith(MockitoExtension.class)
class RemoveCartItemCommandFromResourceAssemblerTest {

  @Mock private RemoveCartItemResource resource;

  @Test
  void toCommandFromResourceMapsResourceToCommand() {
    when(resource.availabilityId()).thenReturn(101L);

    var command = RemoveCartItemCommandFromResourceAssembler.toCommandFromResource(1L, resource);

    assertEquals(1L, command.userId().userId());
    assertEquals(101L, command.availabilityId().availabilityId());
  }
}
