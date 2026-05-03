package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.FavoriteResource;

public class FavoriteResourceFromEntityAssembler {
    public static FavoriteResource toResourceFromEntity(Favorite entity) {
        return new FavoriteResource(entity.getId(), entity.getUserId().userId(), entity.getExperienceId().experienceId());
    }
}

