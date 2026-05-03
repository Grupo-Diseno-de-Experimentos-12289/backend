package pe.edu.upc.travelmatch.profiles.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.services.ReviewQueryService;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewRepository reviewRepository;

    public ReviewQueryServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> handle(GetReviewsByUserIdQuery query) {
        return this.reviewRepository.findAllByUserId(query.userId());
    }

    @Override
    public List<Review> handle(GetReviewsByExperienceIdQuery query) {
        return this.reviewRepository.findAllByExperienceId(query.experienceId());
    }

    @Override
    public Optional<Review> handle(GetReviewByUserIdAndExperienceIdQuery query) {
        return this.reviewRepository.findByUserIdAndExperienceId(query.userId(), query.experienceId());
    }
}
