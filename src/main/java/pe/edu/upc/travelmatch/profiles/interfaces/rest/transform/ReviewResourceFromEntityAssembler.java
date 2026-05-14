package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.ReviewResource;

/** ReviewResourceFromEntityAssembler type. */
public class ReviewResourceFromEntityAssembler {
  /** To resource from entity. */
  public static ReviewResource toResourceFromEntity(Review entity) {
    return new ReviewResource(
        entity.getId(),
        entity.getUserId().userId(),
        entity.getExperienceId().experienceId(),
        entity.getRating().rating(),
        entity.getComment());
  }
}
