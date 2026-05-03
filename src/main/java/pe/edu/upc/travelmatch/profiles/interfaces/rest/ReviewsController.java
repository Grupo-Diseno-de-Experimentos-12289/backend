package pe.edu.upc.travelmatch.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.domain.services.ReviewCommandService;
import pe.edu.upc.travelmatch.profiles.domain.services.ReviewQueryService;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateReviewResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.ReviewResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.UpdateReviewResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.CreateReviewCommandFromResourceAssembler;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.ReviewResourceFromEntityAssembler;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.transform.UpdateReviewCommandFromResourceAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/reviews", produces = APPLICATION_JSON_VALUE)
@Tag(name="Reviews", description = "Review Management Endpoints")
public class ReviewsController {
    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    public ReviewsController(ReviewCommandService reviewCommandService, ReviewQueryService reviewQueryService) {
        this.reviewCommandService = reviewCommandService;
        this.reviewQueryService = reviewQueryService;
    }

    @PostMapping
    public ResponseEntity<ReviewResource> createReview(@RequestBody CreateReviewResource createReviewResource) {
        var createReviewCommand = CreateReviewCommandFromResourceAssembler.toCommandFromResource(createReviewResource);
        var reviewId = reviewCommandService.handle(createReviewCommand);
        System.out.println("Review created with id: " + reviewId);
        var getReviewByUserIdAndExperienceIdQuery = new GetReviewByUserIdAndExperienceIdQuery(new UserId(createReviewResource.userId()), new ExperienceId(createReviewResource.experienceId()));
        var review = reviewQueryService.handle(getReviewByUserIdAndExperienceIdQuery);

        if(review.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var reviewResource = ReviewResourceFromEntityAssembler.toResourceFromEntity(review.get());
        return new ResponseEntity<>(reviewResource, HttpStatus.CREATED);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResource> updateReview(@PathVariable Long reviewId, @RequestBody UpdateReviewResource updateReviewResource) {
        var updateReviewCommand = UpdateReviewCommandFromResourceAssembler.toCommandFromResource(reviewId, updateReviewResource);
        var updatedReview = reviewCommandService.handle(updateReviewCommand);

        if(updatedReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var reviewResource = ReviewResourceFromEntityAssembler.toResourceFromEntity(updatedReview.get());
        return new ResponseEntity<>(reviewResource, HttpStatus.OK);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ReviewResource>> getReviewsByUserId(@PathVariable Long userId){
        UserId userIdValueObject = new UserId(userId);
        var getReviewsByUserIdQuery = new GetReviewsByUserIdQuery(userIdValueObject);
        var reviews = reviewQueryService.handle(getReviewsByUserIdQuery);
        var reviewResource = reviews.stream().map(ReviewResourceFromEntityAssembler::toResourceFromEntity).toList();
        return new ResponseEntity<>(reviewResource, HttpStatus.OK);
    }

    @GetMapping("/by-experience/{experienceId}")
    public ResponseEntity<List<ReviewResource>> getReviewsByExperienceId(@PathVariable Long experienceId) {
        ExperienceId experienceIdValueObject = new ExperienceId(experienceId);
        var getReviewsByExperienceIdQuery = new GetReviewsByExperienceIdQuery(experienceIdValueObject);
        var reviews = reviewQueryService.handle(getReviewsByExperienceIdQuery);
        var reviewResource = reviews.stream().map(ReviewResourceFromEntityAssembler::toResourceFromEntity).toList();
        return new ResponseEntity<>(reviewResource, HttpStatus.OK);
    }

}
