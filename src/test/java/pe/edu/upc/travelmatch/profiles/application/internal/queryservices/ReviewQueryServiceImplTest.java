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
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Rating;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.ReviewRepository;

@ExtendWith(MockitoExtension.class)
class ReviewQueryServiceImplTest {

  @Mock private ReviewRepository reviewRepository;

  @InjectMocks private ReviewQueryServiceImpl reviewQueryService;

  @Test
  void testHandle_GetReviewsByUserId() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    GetReviewsByUserIdQuery query = new GetReviewsByUserIdQuery(userId);
    Review review = new Review(userId, experienceId, new Rating(5), "Great experience!");
    List<Review> expectedReviews = List.of(review);

    when(reviewRepository.findAllByUserId(userId)).thenReturn(expectedReviews);

    // Act
    List<Review> result = reviewQueryService.handle(query);

    // Assert
    assertEquals(expectedReviews.size(), result.size());
    assertEquals(userId, result.get(0).getUserId());
    verify(reviewRepository, times(1)).findAllByUserId(userId);
  }

  @Test
  void testHandle_GetReviewsByExperienceId() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    GetReviewsByExperienceIdQuery query = new GetReviewsByExperienceIdQuery(experienceId);
    Review review = new Review(userId, experienceId, new Rating(5), "Great experience!");
    List<Review> expectedReviews = List.of(review);

    when(reviewRepository.findAllByExperienceId(experienceId)).thenReturn(expectedReviews);

    // Act
    List<Review> result = reviewQueryService.handle(query);

    // Assert
    assertEquals(expectedReviews.size(), result.size());
    assertEquals(experienceId, result.get(0).getExperienceId());
    verify(reviewRepository, times(1)).findAllByExperienceId(experienceId);
  }

  @Test
  void testHandle_GetReviewByUserIdAndExperienceId() {
    // Arrange
    UserId userId = new UserId(1L);
    ExperienceId experienceId = new ExperienceId(10L);
    GetReviewByUserIdAndExperienceIdQuery query =
        new GetReviewByUserIdAndExperienceIdQuery(userId, experienceId);
    Review expectedReview = new Review(userId, experienceId, new Rating(5), "Great experience!");

    when(reviewRepository.findByUserIdAndExperienceId(userId, experienceId))
        .thenReturn(Optional.of(expectedReview));

    // Act
    Optional<Review> result = reviewQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(userId, result.get().getUserId());
    assertEquals(experienceId, result.get().getExperienceId());
    verify(reviewRepository, times(1)).findByUserIdAndExperienceId(userId, experienceId);
  }
}
