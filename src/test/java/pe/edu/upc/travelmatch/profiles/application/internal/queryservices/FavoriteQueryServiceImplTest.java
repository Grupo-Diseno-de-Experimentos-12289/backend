package pe.edu.upc.travelmatch.profiles.application.internal.queryservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoriteByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoritesByExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoritesByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.FavoriteRepository;

@ExtendWith(MockitoExtension.class)
class FavoriteQueryServiceImplTest {

  @Mock private FavoriteRepository favoriteRepository;

  @InjectMocks private FavoriteQueryServiceImpl favoriteQueryService;

  @Test
  void testHandle_GetFavoritesByUserId() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    GetFavoritesByUserIdQuery query = new GetFavoritesByUserIdQuery(userId);
    Favorite favorite = new Favorite(userId, experienceId);
    List<Favorite> expectedFavorites = List.of(favorite);

    when(favoriteRepository.findAllByUserId(userId)).thenReturn(expectedFavorites);

    // Act
    List<Favorite> result = favoriteQueryService.handle(query);

    // Assert
    assertEquals(expectedFavorites.size(), result.size());
    assertEquals(userId, result.get(0).getUserId());
    verify(favoriteRepository, times(1)).findAllByUserId(userId);
  }

  @Test
  void testHandle_GetFavoritesByExperienceId() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    GetFavoritesByExperienceIdQuery query = new GetFavoritesByExperienceIdQuery(experienceId);
    Favorite favorite = new Favorite(userId, experienceId);
    List<Favorite> expectedFavorites = List.of(favorite);

    when(favoriteRepository.findAllByExperienceId(experienceId)).thenReturn(expectedFavorites);

    // Act
    List<Favorite> result = favoriteQueryService.handle(query);

    // Assert
    assertEquals(expectedFavorites.size(), result.size());
    assertEquals(experienceId, result.get(0).getExperienceId());
    verify(favoriteRepository, times(1)).findAllByExperienceId(experienceId);
  }

  @Test
  void testHandle_GetFavoriteByUserIdAndExperienceId() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    GetFavoriteByUserIdAndExperienceIdQuery query =
        new GetFavoriteByUserIdAndExperienceIdQuery(userId, experienceId);
    Favorite expectedFavorite = new Favorite(userId, experienceId);

    when(favoriteRepository.findByUserIdAndExperienceId(userId, experienceId))
        .thenReturn(Optional.of(expectedFavorite));

    // Act
    Optional<Favorite> result = favoriteQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(userId, result.get().getUserId());
    assertEquals(experienceId, result.get().getExperienceId());
    verify(favoriteRepository, times(1)).findByUserIdAndExperienceId(userId, experienceId);
  }
}
