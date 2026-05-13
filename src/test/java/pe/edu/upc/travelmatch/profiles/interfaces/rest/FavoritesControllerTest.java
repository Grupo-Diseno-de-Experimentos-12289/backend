package pe.edu.upc.travelmatch.profiles.interfaces.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateFavoriteCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.DeleteFavoriteCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoriteByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoritesByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.domain.services.FavoriteCommandService;
import pe.edu.upc.travelmatch.profiles.domain.services.FavoriteQueryService;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateFavoriteResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.FavoriteResource;

@ExtendWith(MockitoExtension.class)
class FavoritesControllerTest {

  @Mock private FavoriteCommandService favoriteCommandService;

  @Mock private FavoriteQueryService favoriteQueryService;

  @InjectMocks private FavoritesController favoritesController;

  private UserId userId;
  private ExperienceId experienceId;
  private Favorite favorite;

  @BeforeEach
  void setUp() {
    userId = new UserId(1L);
    experienceId = new ExperienceId(10L);
    favorite = new Favorite(userId, experienceId);
  }

  @Test
  void testCreateFavorite_Created() {
    // Arrange
    CreateFavoriteResource resource = new CreateFavoriteResource(1L, 10L);
    when(favoriteCommandService.handle(any(CreateFavoriteCommand.class))).thenReturn(1L);
    when(favoriteQueryService.handle(any(GetFavoriteByUserIdAndExperienceIdQuery.class)))
        .thenReturn(Optional.of(favorite));

    // Act
    ResponseEntity<FavoriteResource> response = favoritesController.createFavorite(resource);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().userId());
    assertEquals(10L, response.getBody().experienceId());
  }

  @Test
  void testCreateFavorite_NotFound() {
    // Arrange
    CreateFavoriteResource resource = new CreateFavoriteResource(1L, 10L);
    when(favoriteCommandService.handle(any(CreateFavoriteCommand.class))).thenReturn(1L);
    when(favoriteQueryService.handle(any(GetFavoriteByUserIdAndExperienceIdQuery.class)))
        .thenReturn(Optional.empty());

    // Act
    ResponseEntity<FavoriteResource> response = favoritesController.createFavorite(resource);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testGetFavoritesByUserId_Ok() {
    // Arrange
    when(favoriteQueryService.handle(any(GetFavoritesByUserIdQuery.class)))
        .thenReturn(List.of(favorite));

    // Act
    ResponseEntity<List<FavoriteResource>> response = favoritesController.getFavoritesByUserId(1L);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals(1L, response.getBody().get(0).userId());
    assertEquals(10L, response.getBody().get(0).experienceId());
  }

  @Test
  void testDeleteFavorite_NoContent() {
    // Arrange
    doNothing().when(favoriteCommandService).handle(any(DeleteFavoriteCommand.class));

    // Act
    ResponseEntity<?> response = favoritesController.deleteFavorite(10L, 1L);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(favoriteCommandService, times(1)).handle(any(DeleteFavoriteCommand.class));
  }
}
