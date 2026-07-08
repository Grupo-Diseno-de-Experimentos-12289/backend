package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

/** CreateExperienceResource(. */
public record CreateExperienceResource(
    String title,
    String description,
    String category,
    Long destinationId,
    String duration,
    String meetingPoint,
    String cancellationPolicyType,
    String cancellationPolicyDescription) {}
