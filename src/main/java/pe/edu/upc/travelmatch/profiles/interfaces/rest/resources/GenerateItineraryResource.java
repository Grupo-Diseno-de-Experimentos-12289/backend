package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

import java.util.List;

/** GenerateItineraryResource value carrier. */
public record GenerateItineraryResource(
        Long destinationId, List<String> categories, int numberOfDays) {}