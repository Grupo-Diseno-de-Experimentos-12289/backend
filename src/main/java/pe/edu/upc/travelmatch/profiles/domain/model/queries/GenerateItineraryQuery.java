package pe.edu.upc.travelmatch.profiles.domain.model.queries;

import java.util.List;

/**
 * Query to generate a personalized itinerary.
 *
 * @param destinationId the destination ID, or null to consider all destinations
 * @param categories the list of preferred categories, or empty to consider all categories
 * @param numberOfDays the number of days the itinerary should cover
 */
public record GenerateItineraryQuery(
        Long destinationId, List<String> categories, int numberOfDays) {
    /** Documentation. */
    public GenerateItineraryQuery {
        if (numberOfDays <= 0) {
            throw new IllegalArgumentException("Number of days must be greater than zero");
        }
        if (categories == null) {
            categories = List.of();
        }
    }
}