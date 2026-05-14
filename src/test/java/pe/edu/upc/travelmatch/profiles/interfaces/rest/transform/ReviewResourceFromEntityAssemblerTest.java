package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Rating;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

@ExtendWith(MockitoExtension.class)
class ReviewResourceFromEntityAssemblerTest {

  @Mock private Review entity;

  @Test
  void toResourceFromEntityMapsEntityToResource() {
    when(entity.getId()).thenReturn(30L);
    when(entity.getUserId()).thenReturn(new UserId(1L));
    when(entity.getExperienceId()).thenReturn(new ExperienceId(50L));
    when(entity.getRating()).thenReturn(new Rating(5));
    when(entity.getComment()).thenReturn("Great experience");

    var resource = ReviewResourceFromEntityAssembler.toResourceFromEntity(entity);

    assertEquals(30L, resource.reviewId());
    assertEquals(1L, resource.userId());
    assertEquals(50L, resource.experienceId());
    assertEquals(5, resource.rating());
    assertEquals("Great experience", resource.comment());
  }
}
