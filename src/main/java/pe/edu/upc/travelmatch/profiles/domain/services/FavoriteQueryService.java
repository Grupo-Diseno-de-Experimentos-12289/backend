package pe.edu.upc.travelmatch.profiles.domain.services;

import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoriteByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoritesByExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoritesByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface FavoriteQueryService {
    List<Favorite> handle(GetFavoritesByUserIdQuery query);
    List<Favorite> handle(GetFavoritesByExperienceIdQuery query);
    Optional<Favorite> handle(GetFavoriteByUserIdAndExperienceIdQuery query);
}
