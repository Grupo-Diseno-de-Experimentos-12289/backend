package pe.edu.upc.travelmatch.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record CreateFavoriteResource (
        @NotNull
        Long userId,
        @NotNull
        Long experienceId
) {
}
