package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

public record ReviewResource (Long reviewId, Long userId, Long experienceId, int rating, String comment) {
}
