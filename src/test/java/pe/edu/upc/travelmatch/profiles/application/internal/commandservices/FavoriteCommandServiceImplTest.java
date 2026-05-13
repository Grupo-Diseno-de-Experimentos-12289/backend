package pe.edu.upc.travelmatch.profiles.application.internal.commandservices;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateFavoriteCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.DeleteFavoriteCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.FavoriteRepository;

@ExtendWith(MockitoExtension.class)
class FavoriteCommandServiceImplTest {

  @Mock private FavoriteRepository favoriteRepository;

  @Mock private ExternalIamService externalIamService;

  @Mock private ExternalExperienceService externalExperienceService;

  @InjectMocks private FavoriteCommandServiceImpl favoriteCommandService;

  @Test
  void testHandle_CreateFavoriteSuccessfully() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    CreateFavoriteCommand command = new CreateFavoriteCommand(userId, experienceId);
    Favorite favorite = new Favorite(userId, experienceId);

    when(externalIamService.existsUserById(userId)).thenReturn(true);
    when(externalExperienceService.existsExperienceById(experienceId)).thenReturn(true);
    when(favoriteRepository.save(any(Favorite.class)))
        .thenAnswer(
            invocation -> {
              Favorite savedFavorite = invocation.getArgument(0);
              return savedFavorite;
            });

    // Act
    Long resultId = favoriteCommandService.handle(command);

    // Assert
    verify(externalIamService, times(1)).existsUserById(userId);
    verify(externalExperienceService, times(1)).existsExperienceById(experienceId);
    verify(favoriteRepository, times(1)).save(any(Favorite.class));
  }

  @Test
  void testHandle_CreateFavorite_UserNotFound_ThrowsException() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    CreateFavoriteCommand command = new CreateFavoriteCommand(userId, experienceId);

    when(externalIamService.existsUserById(userId)).thenReturn(false);

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              favoriteCommandService.handle(command);
            });

    assertTrue(exception.getMessage().contains("User with id"));
    verify(externalExperienceService, never()).existsExperienceById(experienceId);
    verify(favoriteRepository, never()).save(any(Favorite.class));
  }

  @Test
  void testHandle_CreateFavorite_ExperienceNotFound_ThrowsException() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    CreateFavoriteCommand command = new CreateFavoriteCommand(userId, experienceId);

    when(externalIamService.existsUserById(userId)).thenReturn(true);
    when(externalExperienceService.existsExperienceById(experienceId)).thenReturn(false);

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              favoriteCommandService.handle(command);
            });

    assertTrue(exception.getMessage().contains("Experience with id"));
    verify(favoriteRepository, never()).save(any(Favorite.class));
  }

  @Test
  void testHandle_DeleteFavoriteSuccessfully() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    DeleteFavoriteCommand command = new DeleteFavoriteCommand(userId, experienceId);
    Favorite favorite = new Favorite(userId, experienceId);

    when(favoriteRepository.findByUserIdAndExperienceId(userId, experienceId))
        .thenReturn(Optional.of(favorite));

    // Act
    favoriteCommandService.handle(command);

    // Assert
    verify(favoriteRepository, times(1)).findByUserIdAndExperienceId(userId, experienceId);
    verify(favoriteRepository, times(1)).delete(favorite);
  }

  @Test
  void testHandle_DeleteFavorite_NotFound_ThrowsException() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    DeleteFavoriteCommand command = new DeleteFavoriteCommand(userId, experienceId);

    when(favoriteRepository.findByUserIdAndExperienceId(userId, experienceId))
        .thenReturn(Optional.empty());

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              favoriteCommandService.handle(command);
            });

    assertTrue(exception.getMessage().contains("Favorite not found for user with id"));
    verify(favoriteRepository, never()).delete(any(Favorite.class));
  }
}
