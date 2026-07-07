package pe.edu.upc.travelmatch.profiles.domain.services;

import java.util.Optional;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateReviewCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.DeleteReviewCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.UpdateReviewCommand;

/** ReviewCommandService contract. */
public interface ReviewCommandService {
  /** Handle. */
  Long handle(CreateReviewCommand command);

  /** Handle. */
  Optional<Review> handle(UpdateReviewCommand command);

  /** Handle. */
  void handle(DeleteReviewCommand command);
}
