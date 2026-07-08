package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

/** ItineraryActivityResource value carrier. */
public record ItineraryActivityResource(
        Long experienceId, String title, String category, String meetingPoint, String duration) {}