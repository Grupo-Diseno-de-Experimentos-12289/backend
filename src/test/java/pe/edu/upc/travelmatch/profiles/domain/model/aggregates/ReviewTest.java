package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Rating;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void testReviewConstructorAndGetters() {
        // Arrange
        UserId userId = new UserId(1L);
        ExperienceId experienceId = new ExperienceId(10L);
        Rating rating = new Rating(5);
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
        Review review = new Review(new UserId(1L), new ExperienceId(10L), new Rating(4), "Good");
        Rating newRating = new Rating(5);

        // Act
        review.updateRating(newRating);

        // Assert
        assertEquals(newRating, review.getRating());
    }

    @Test
    void testUpdateRating_ThrowsExceptionWhenNull() {
        // Arrange
        Review review = new Review(new UserId(1L), new ExperienceId(10L), new Rating(4), "Good");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> review.updateRating(null));
        assertTrue(exception.getMessage().contains("Rating cannot be null"));
    }

    @Test
    void testUpdateComment_Successfully() {
        // Arrange
        Review review = new Review(new UserId(1L), new ExperienceId(10L), new Rating(4), "Good");
        String newComment = "Excellent!";

        // Act
        review.updateComment(newComment);

        // Assert
        assertEquals(newComment, review.getComment());
    }

    @Test
    void testUpdateComment_ThrowsExceptionWhenNull() {
        // Arrange
        Review review = new Review(new UserId(1L), new ExperienceId(10L), new Rating(4), "Good");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> review.updateComment(null));
        assertTrue(exception.getMessage().contains("Comment cannot be null or empty"));
    }

    @Test
    void testUpdateComment_ThrowsExceptionWhenEmpty() {
        // Arrange
        Review review = new Review(new UserId(1L), new ExperienceId(10L), new Rating(4), "Good");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> review.updateComment("   "));
        assertTrue(exception.getMessage().contains("Comment cannot be null or empty"));
    }
}
