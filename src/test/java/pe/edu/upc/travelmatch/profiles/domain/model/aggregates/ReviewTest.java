package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Rating;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

@ExtendWith(MockitoExtension.class)
class ReviewTest {

  @Mock private UserId userId;

  @Mock private ExperienceId experienceId;

  @Mock private Rating rating;

  @Mock private Rating newRating;

  @Test
  void testReviewConstructorAndGetters() {
    // Arrange
    String comment = "Great experience!";

    // Act
    Review review = new Review(userId, experienceId, rating, comment);

    // Assert
    assertEquals(userId, review.getUserId());
    assertEquals(experienceId, review.getExperienceId());
    assertEquals(rating, review.getRating());
    assertEquals(comment, review.getComment());
  }

  @Test
  void testUpdateRating_Successfully() {
    // Arrange
    Review review = new Review(userId, experienceId, rating, "Good");

    // Act
    review.updateRating(newRating);

    // Assert
    assertEquals(newRating, review.getRating());
  }

  @Test
  void testUpdateRating_ThrowsExceptionWhenNull() {
    // Arrange
    Review review = new Review(userId, experienceId, rating, "Good");

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> review.updateRating(null));
    assertTrue(exception.getMessage().contains("Rating cannot be null"));
  }

  @Test
  void testUpdateComment_Successfully() {
    // Arrange
    Review review = new Review(userId, experienceId, rating, "Good");
    String newComment = "Excellent!";

    // Act
    review.updateComment(newComment);

    // Assert
    assertEquals(newComment, review.getComment());
  }

  @Test
  void testUpdateComment_ThrowsExceptionWhenNull() {
    // Arrange
    Review review = new Review(userId, experienceId, rating, "Good");

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> review.updateComment(null));
    assertTrue(exception.getMessage().contains("Comment cannot be null or empty"));
  }

  @Test
  void testUpdateComment_ThrowsExceptionWhenEmpty() {
    // Arrange
    Review review = new Review(userId, experienceId, rating, "Good");

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> review.updateComment("   "));
    assertTrue(exception.getMessage().contains("Comment cannot be null or empty"));
  }
}
