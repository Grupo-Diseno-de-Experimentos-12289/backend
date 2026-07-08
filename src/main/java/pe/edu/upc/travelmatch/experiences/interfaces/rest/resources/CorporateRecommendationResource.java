package pe.edu.upc.travelmatch.experiences.interfaces.rest.resources;

import java.util.List;

/**
 * A recommended experience for a corporate traveler, together with the availability slots that
 * fit inside their free time window.
 *
 * @param experienceId the experience ID
 * @param title the experience title
 * @param category the experience category (matches one of the traveler's interests)
 * @param destinationId the destination ID (matches the traveler's location)
 * @param duration the typical duration of the experience
 * @param meetingPoint the meeting point
 * @param matchingAvailabilities the availability slots that fit the traveler's schedule
 */
public record CorporateRecommendationResource(
    Long experienceId,
    String title,
    String category,
    Long destinationId,
    String duration,
    String meetingPoint,
    List<AvailabilitySummaryResource> matchingAvailabilities) {}
