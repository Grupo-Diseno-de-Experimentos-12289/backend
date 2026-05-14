package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateCartResource;

@ExtendWith(MockitoExtension.class)
class CreateCartCommandFromResourceAssemblerTest {

  @Mock private CreateCartResource resource;

  @Test
  void toCommandFromResourceMapsResourceToCommand() {
    when(resource.userId()).thenReturn(1L);

    var command = CreateCartCommandFromResourceAssembler.toCommandFromResource(resource);

    assertEquals(1L, command.userId().userId());
  }
}
