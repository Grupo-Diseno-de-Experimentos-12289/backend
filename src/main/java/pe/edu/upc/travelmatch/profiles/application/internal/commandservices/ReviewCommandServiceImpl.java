package pe.edu.upc.travelmatch.profiles.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateReviewCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.DeleteReviewCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.UpdateReviewCommand;
import pe.edu.upc.travelmatch.profiles.domain.services.ReviewCommandService;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.ReviewRepository;

import java.util.Optional;

@Service
public class ReviewCommandServiceImpl implements ReviewCommandService {
    private final ReviewRepository reviewRepository;
    private final ExternalIamService externalIamService;
    private final ExternalExperienceService externalExperiencesService;

    public ReviewCommandServiceImpl(ReviewRepository reviewRepository,
                                    ExternalIamService externalIamService,
                                    ExternalExperienceService externalExperiencesService) {
        this.reviewRepository = reviewRepository;
        this.externalIamService = externalIamService;
        this.externalExperiencesService = externalExperiencesService;
    }

    @Override
    public Long handle(CreateReviewCommand command) {
        if(!externalIamService.existsUserById(command.userId()))
            throw new IllegalArgumentException("User with id " + command.userId().userId()+ " not found");
        if(!externalExperiencesService.existsExperienceById(command.experienceId()))
            throw new IllegalArgumentException("Experience with id " + command.experienceId().experienceId() + " not found");
        var review = new Review(command.userId(), command.experienceId(), command.rating(), command.comment());
        reviewRepository.save(review);
        return review.getId();
    }

    @Override
    public Optional<Review> handle(UpdateReviewCommand command) {
        var existingReview =  reviewRepository.findById(command.reviewId());
        if (existingReview.isEmpty()) {
            return Optional.empty();
        }

        var review = existingReview.get();
        review.updateRating(command.rating());
        review.updateComment(command.comment());

        reviewRepository.save(review);

        return Optional.of(review);
    }

    @Override
    public void handle(DeleteReviewCommand command) {
        var existingReview =  reviewRepository.findById(command.reviewId());
        if (existingReview.isEmpty()) {
            throw new IllegalArgumentException("Review with id " + command.reviewId() + " not found");
        }
        reviewRepository.deleteById(command.reviewId());
    }
}
