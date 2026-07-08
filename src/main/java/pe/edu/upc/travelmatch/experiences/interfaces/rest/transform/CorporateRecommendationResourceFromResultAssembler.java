package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.queries.CorporateRecommendation;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CorporateRecommendationResource;

/** Assembles a {@link CorporateRecommendationResource} from a {@link CorporateRecommendation}. */
public class CorporateRecommendationResourceFromResultAssembler {

  private CorporateRecommendationResourceFromResultAssembler() {}

  /**
   * To resource from result.
   *
   * @param result the corporate recommendation read model
   * @return the REST resource
   */
  public static CorporateRecommendationResource toResourceFromResult(
      CorporateRecommendation result) {
    var experience = result.experience();
    var availabilities =
        result.matchingAvailabilities().stream()
            .map(AvailabilitySummaryResourceFromEntityAssembler::toResourceFromEntity)
            .toList();

    return new CorporateRecommendationResource(
        experience.getId(),
        experience.getTitle(),
        experience.getCategory().getCategoryName(),
        experience.getDestinationId().value(),
        experience.getDuration(),
        experience.getMeetingPoint(),
        availabilities);
  }
}
