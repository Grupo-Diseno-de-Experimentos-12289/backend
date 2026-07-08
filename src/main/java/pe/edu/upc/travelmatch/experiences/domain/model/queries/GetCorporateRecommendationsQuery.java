package pe.edu.upc.travelmatch.experiences.domain.model.queries;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Query to get personalized experience recommendations for a corporate traveler, based on their
 * location (destination), interests (categories) and free time window in their work schedule.
 *
 * @param destinationId the destination where the traveler currently is
 * @param interests the list of category names the traveler is interested in (empty/null means
 *     any category)
 * @param windowStart the start of the traveler's free time window
 * @param windowEnd the end of the traveler's free time window
 */
public record GetCorporateRecommendationsQuery(
    Long destinationId, List<String> interests, LocalDateTime windowStart, LocalDateTime windowEnd) {}
