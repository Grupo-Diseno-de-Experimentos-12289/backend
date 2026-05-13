package pe.edu.upc.travelmatch.profiles.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetReviewsByUserIdQuery;

/** ReviewQueryService contract. */
public interface ReviewQueryService {
  /** Handle. */
  List<Review> handle(GetReviewsByUserIdQuery query);

  /** Handle. */
  List<Review> handle(GetReviewsByExperienceIdQuery query);

  /** Handle. */
  Optional<Review> handle(GetReviewByUserIdAndExperienceIdQuery query);
}
