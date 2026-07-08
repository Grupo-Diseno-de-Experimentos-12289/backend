package pe.edu.upc.travelmatch.experiences.domain.model.queries;

import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;

import java.util.List;

/**
 * Read model pairing an {@link Experience} with the subset of its availabilities that fit inside
 * a corporate traveler's free time window and still have stock. Sorted so the caller can prefer
 * the recommendation that best fits the traveler's agenda (soonest matching slot first).
 *
 * @param experience the recommended experience
 * @param matchingAvailabilities the availabilities that fit the requested time window, sorted by
 *     start date/time ascending
 */
public record CorporateRecommendation(
    Experience experience, List<Availability> matchingAvailabilities) {}
