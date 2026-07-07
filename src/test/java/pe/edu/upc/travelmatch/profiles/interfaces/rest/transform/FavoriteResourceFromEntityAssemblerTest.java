package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

@ExtendWith(MockitoExtension.class)
class FavoriteResourceFromEntityAssemblerTest {

  @Mock private Favorite entity;

  @Test
  void toResourceFromEntityMapsEntityToResource() {
    when(entity.getId()).thenReturn(20L);
    when(entity.getUserId()).thenReturn(new UserId(1L));
    when(entity.getExperienceId()).thenReturn(new ExperienceId(50L));

    var resource = FavoriteResourceFromEntityAssembler.toResourceFromEntity(entity);

    assertEquals(20L, resource.favoriteId());
    assertEquals(1L, resource.userId());
    assertEquals(50L, resource.experienceId());
  }
}
