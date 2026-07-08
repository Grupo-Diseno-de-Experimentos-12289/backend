package pe.edu.upc.travelmatch.experiences.domain.model.commands;

import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;

/**
 * Command to create an experience.
 *
 * @param title the title
 * @param description the description
 * @param agencyId the agency ID
 * @param category the category
 * @param destinationId the destination ID
 * @param duration the duration
 * @param meetingPoint the meeting point
 * @param cancellationPolicyType the cancellation policy type (e.g. FLEXIBLE, MODERATE, STRICT,
 *     NON_REFUNDABLE)
 * @param cancellationPolicyDescription the human-readable cancellation policy details
 */
public record CreateExperienceCommand(
    String title,
    String description,
    Long agencyId,
    String category,
    DestinationId destinationId,
    String duration,
    String meetingPoint,
    CancellationPolicyType cancellationPolicyType,
    String cancellationPolicyDescription) {}
