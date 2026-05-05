package pe.edu.upc.travelmatch.profiles.interfaces.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateReviewCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.UpdateReviewCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Rating;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.domain.services.ReviewCommandService;
import pe.edu.upc.travelmatch.profiles.domain.services.ReviewQueryService;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateReviewResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.ReviewResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.UpdateReviewResource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewsControllerTest {

    @Mock
    private ReviewCommandService reviewCommandService;

    @Mock
    private ReviewQueryService reviewQueryService;

    @InjectMocks
    private ReviewsController reviewsController;

    private UserId userId;
    private ExperienceId experienceId;
    private Review review;

    @BeforeEach
    void setUp() {
        userId = new UserId(1L);
        experienceId = new ExperienceId(10L);
        review = new Review(userId, experienceId, new Rating(5), "Great!");
    }

    @Test
    void testCreateReview_Created() {
        // Arrange
        CreateReviewResource resource = new CreateReviewResource(1L, 10L, 5, "Great!");
        when(reviewCommandService.handle(any(CreateReviewCommand.class))).thenReturn(1L);
        when(reviewQueryService.handle(any(GetReviewByUserIdAndExperienceIdQuery.class))).thenReturn(Optional.of(review));

        // Act
        ResponseEntity<ReviewResource> response = reviewsController.createReview(resource);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().userId());
        assertEquals(10L, response.getBody().experienceId());
        assertEquals(5, response.getBody().rating());
    }

    @Test
    void testCreateReview_NotFound() {
        // Arrange
        CreateReviewResource resource = new CreateReviewResource(1L, 10L, 5, "Great!");
        when(reviewCommandService.handle(any(CreateReviewCommand.class))).thenReturn(1L);
        when(reviewQueryService.handle(any(GetReviewByUserIdAndExperienceIdQuery.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ReviewResource> response = reviewsController.createReview(resource);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateReview_Ok() {
        // Arrange
        UpdateReviewResource resource = new UpdateReviewResource(4, "Good!");
        Review updatedReview = new Review(userId, experienceId, new Rating(4), "Good!");
        when(reviewCommandService.handle(any(UpdateReviewCommand.class))).thenReturn(Optional.of(updatedReview));

        // Act
        ResponseEntity<ReviewResource> response = reviewsController.updateReview(1L, resource);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().rating());
        assertEquals("Good!", response.getBody().comment());
    }

    @Test
    void testUpdateReview_NotFound() {
        // Arrange
        UpdateReviewResource resource = new UpdateReviewResource(4, "Good!");
        when(reviewCommandService.handle(any(UpdateReviewCommand.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ReviewResource> response = reviewsController.updateReview(1L, resource);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetReviewsByUserId_Ok() {
        // Arrange
        when(reviewQueryService.handle(any(GetReviewsByUserIdQuery.class))).thenReturn(List.of(review));

        // Act
        ResponseEntity<List<ReviewResource>> response = reviewsController.getReviewsByUserId(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).userId());
    }

    @Test
    void testGetReviewsByExperienceId_Ok() {
        // Arrange
        when(reviewQueryService.handle(any(GetReviewsByExperienceIdQuery.class))).thenReturn(List.of(review));

        // Act
        ResponseEntity<List<ReviewResource>> response = reviewsController.getReviewsByExperienceId(10L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(10L, response.getBody().get(0).experienceId());
    }
}
