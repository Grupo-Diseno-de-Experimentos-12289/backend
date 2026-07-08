package pe.edu.upc.travelmatch.profiles.domain.model.valueobjects;

import java.util.List;

/** Itinerary value carrier. */
public record Itinerary(Long destinationId, int numberOfDays, List<ItineraryDay> days) {}
