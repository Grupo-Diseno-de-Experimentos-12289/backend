package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record CreateReviewResource (
        @NotNull
        Long userId,
        @NotNull
        Long experienceId,
        int rating,
        String comment) {
}
