package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.ExperienceBookingInfoResource;

import java.util.List;

/**
 * Assembles the {@link ExperienceBookingInfoResource} shown to a tourist before booking, combining
 * an experience's cancellation policy with its list of upcoming availabilities.
 */
public class ExperienceBookingInfoResourceAssembler {

  private ExperienceBookingInfoResourceAssembler() {}

  /**
   * Builds the resource from the experience and its availabilities.
   *
   * @param experience the experience
   * @param availabilities the experience's non-deleted availabilities
   * @return the combined booking-info resource
   */
  public static ExperienceBookingInfoResource toResource(
      Experience experience, List<Availability> availabilities) {
    var availabilityResources =
        availabilities.stream()
            .map(AvailabilitySummaryResourceFromEntityAssembler::toResourceFromEntity)
            .toList();

    return new ExperienceBookingInfoResource(
        experience.getId(),
        experience.getTitle(),
        experience.getCancellationPolicyType() == null
            ? null
            : experience.getCancellationPolicyType().name(),
        experience.getCancellationPolicyDescription(),
        availabilityResources);
  }
}
