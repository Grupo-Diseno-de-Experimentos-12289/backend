package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

import java.util.List;

/** ItineraryResource value carrier. */
public record ItineraryResource(
        Long destinationId, int numberOfDays, List<ItineraryDayResource> days) {}