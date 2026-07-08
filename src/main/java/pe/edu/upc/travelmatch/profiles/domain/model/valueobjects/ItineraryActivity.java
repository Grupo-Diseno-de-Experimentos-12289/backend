package pe.edu.upc.travelmatch.profiles.domain.model.valueobjects;

/** ItineraryActivity value carrier. */
public record ItineraryActivity(
        Long experienceId, String title, String category, String meetingPoint, String duration) {}