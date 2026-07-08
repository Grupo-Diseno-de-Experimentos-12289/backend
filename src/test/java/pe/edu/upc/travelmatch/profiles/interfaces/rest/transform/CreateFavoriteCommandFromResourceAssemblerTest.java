package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateFavoriteResource;

@ExtendWith(MockitoExtension.class)
class CreateFavoriteCommandFromResourceAssemblerTest {

  @Mock private CreateFavoriteResource resource;

  @Test
  void toCommandFromResourceMapsResourceToCommand() {
    when(resource.userId()).thenReturn(1L);
    when(resource.experienceId()).thenReturn(50L);

    var command = CreateFavoriteCommandFromResourceAssembler.toCommandFromResource(resource);

    assertEquals(1L, command.userId().userId());
    assertEquals(50L, command.experienceId().experienceId());
  }
}
