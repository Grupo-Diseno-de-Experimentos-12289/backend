package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

/** ReviewResource value carrier. */
public record ReviewResource(
    Long reviewId, Long userId, Long experienceId, int rating, String comment) {}
