package pe.edu.upc.travelmatch.profiles.domain.services;

import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface ReviewQueryService {
    List<Review> handle(GetReviewsByUserIdQuery query);
    List<Review> handle(GetReviewsByExperienceIdQuery query);
    Optional<Review> handle(GetReviewByUserIdAndExperienceIdQuery query);
}
