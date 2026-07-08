package pe.edu.upc.travelmatch.experiences.domain.model.commands;

import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;

/**
 * Command to update an experience.
 *
 * @param id the ID
 * @param title the title
 * @param description the description
 * @param category the category
 * @param destinationId the destination ID
 * @param duration the duration
 * @param meetingPoint the meeting point
 * @param cancellationPolicyType the cancellation policy type
 * @param cancellationPolicyDescription the human-readable cancellation policy details
 */
public record UpdateExperienceCommand(
    Long id,
    String title,
    String description,
    String category,
    DestinationId destinationId,
    String duration,
    String meetingPoint,
    CancellationPolicyType cancellationPolicyType,
    String cancellationPolicyDescription) {}
