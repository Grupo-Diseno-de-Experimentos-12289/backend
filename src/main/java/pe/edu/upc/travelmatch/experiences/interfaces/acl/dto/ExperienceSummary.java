package pe.edu.upc.travelmatch.experiences.interfaces.acl.dto;

/**
 * Record representing a lightweight summary of an Experience, used for cross-context read models
 * such as itinerary generation.
 */
public record ExperienceSummary(
        Long experienceId,
        String title,
        String category,
        String meetingPoint,
        String duration,
        Long destinationId) {}