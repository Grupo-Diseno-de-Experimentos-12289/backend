package pe.edu.upc.travelmatch.profiles.domain.model.valueobjects;

import java.util.List;

/** ItineraryDay value carrier. */
public record ItineraryDay(int dayNumber, List<ItineraryActivity> activities) {}