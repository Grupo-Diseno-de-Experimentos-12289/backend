package pe.edu.upc.travelmatch.profiles.domain.services;

import pe.edu.upc.travelmatch.profiles.domain.model.queries.GenerateItineraryQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Itinerary;

/** Service to manage itinerary generation. */
public interface ItineraryQueryService {
    /**
     * Handles the GenerateItineraryQuery.
     *
     * @param query the query object
     * @return the generated itinerary
     */
    Itinerary handle(GenerateItineraryQuery query);
}