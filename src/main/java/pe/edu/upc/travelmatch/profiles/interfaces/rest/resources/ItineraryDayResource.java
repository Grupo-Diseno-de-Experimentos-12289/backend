package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

import java.util.List;

/** ItineraryDayResource value carrier. */
public record ItineraryDayResource(int dayNumber, List<ItineraryActivityResource> activities) {}