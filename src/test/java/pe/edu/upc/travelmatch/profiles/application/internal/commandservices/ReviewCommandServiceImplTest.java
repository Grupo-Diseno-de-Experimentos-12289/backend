package pe.edu.upc.travelmatch.profiles.application.internal.commandservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateReviewCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.DeleteReviewCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.UpdateReviewCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Rating;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.ReviewRepository;

@ExtendWith(MockitoExtension.class)
class ReviewCommandServiceImplTest {

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ExternalIamService externalIamService;

  @Mock
  private ExternalExperienceService externalExperienceService;

  @InjectMocks
  private ReviewCommandServiceImpl reviewCommandService;

  @Test
  void testHandle_CreateReviewSuccessfully() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    Rating rating = new Rating(5);
    String comment = "Great experience!";
    CreateReviewCommand command = new CreateReviewCommand(userId, experienceId, rating, comment);

    when(externalIamService.existsUserById(userId)).thenReturn(true);
    when(externalExperienceService.existsExperienceById(experienceId)).thenReturn(true);
    when(reviewRepository.save(any(Review.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    reviewCommandService.handle(command);

    // Assert
    verify(externalIamService, times(1)).existsUserById(userId);
    verify(externalExperienceService, times(1)).existsExperienceById(experienceId);
    verify(reviewRepository, times(1)).save(any(Review.class));
  }

  @Test
  void testHandle_CreateReview_UserNotFound_ThrowsException() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    Rating rating = new Rating(5);
    String comment = "Great experience!";
    CreateReviewCommand command = new CreateReviewCommand(userId, experienceId, rating, comment);

    when(externalIamService.existsUserById(userId)).thenReturn(false);

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              reviewCommandService.handle(command);
            });

    assertTrue(exception.getMessage().contains("User with id"));
    verify(externalExperienceService, never()).existsExperienceById(experienceId);
    verify(reviewRepository, never()).save(any(Review.class));
  }

  @Test
  void testHandle_CreateReview_ExperienceNotFound_ThrowsException() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    Rating rating = new Rating(5);
    String comment = "Great experience!";
    CreateReviewCommand command = new CreateReviewCommand(userId, experienceId, rating, comment);

    when(externalIamService.existsUserById(userId)).thenReturn(true);
    when(externalExperienceService.existsExperienceById(experienceId)).thenReturn(false);

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              reviewCommandService.handle(command);
            });

    assertTrue(exception.getMessage().contains("Experience with id"));
    verify(reviewRepository, never()).save(any(Review.class));
  }

  @Test
  void testHandle_UpdateReviewSuccessfully() {
    // Arrange
    Long reviewId = 100L;
    Rating newRating = new Rating(4);
    String newComment = "Good experience";
    UpdateReviewCommand command = new UpdateReviewCommand(reviewId, newRating, newComment);
    Review review =
        new Review(new UserId(1L), new ExperienceId(10L), new Rating(5), "Great experience!");

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(reviewRepository.save(any(Review.class))).thenReturn(review);

    // Act
    Optional<Review> result = reviewCommandService.handle(command);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(newRating, result.get().getRating());
    assertEquals(newComment, result.get().getComment());
    verify(reviewRepository, times(1)).findById(reviewId);
    verify(reviewRepository, times(1)).save(review);
  }

  @Test
  void testHandle_UpdateReview_NotFound_ReturnsEmpty() {
    // Arrange
    Long reviewId = 100L;
    Rating newRating = new Rating(4);
    String newComment = "Good experience";
    UpdateReviewCommand command = new UpdateReviewCommand(reviewId, newRating, newComment);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

    // Act
    Optional<Review> result = reviewCommandService.handle(command);

    // Assert
    assertTrue(result.isEmpty());
    verify(reviewRepository, times(1)).findById(reviewId);
    verify(reviewRepository, never()).save(any(Review.class));
  }

  @Test
  void testHandle_DeleteReviewSuccessfully() {
    // Arrange
    Long reviewId = 100L;
    DeleteReviewCommand command = new DeleteReviewCommand(reviewId);
    Review review =
        new Review(new UserId(1L), new ExperienceId(10L), new Rating(5), "Great experience!");

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

    // Act
    reviewCommandService.handle(command);

    // Assert
    verify(reviewRepository, times(1)).findById(reviewId);
    verify(reviewRepository, times(1)).deleteById(reviewId);
  }

  @Test
  void testHandle_DeleteReview_NotFound_ThrowsException() {
    // Arrange
    Long reviewId = 100L;
    DeleteReviewCommand command = new DeleteReviewCommand(reviewId);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              reviewCommandService.handle(command);
            });

    assertTrue(exception.getMessage().contains("Review with id"));
    verify(reviewRepository, never()).deleteById(reviewId);
  }
}
