package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

import java.util.List;

/**
 * Aggregated view of everything a tourist needs to see before booking an experience: its
 * upcoming availabilities (with remaining seats) and its cancellation policy.
 *
 * @param experienceId the experience ID
 * @param title the experience title
 * @param cancellationPolicyType the cancellation policy type (FLEXIBLE, MODERATE, STRICT,
 *     NON_REFUNDABLE)
 * @param cancellationPolicyDescription human-readable details of the cancellation policy
 * @param availabilities the upcoming, non-deleted availabilities for this experience
 */
public record ExperienceBookingInfoResource(
    Long experienceId,
    String title,
    String cancellationPolicyType,
    String cancellationPolicyDescription,
    List<AvailabilitySummaryResource> availabilities) {}
