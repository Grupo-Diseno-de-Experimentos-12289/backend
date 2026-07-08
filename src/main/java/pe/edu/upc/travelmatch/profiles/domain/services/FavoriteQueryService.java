package pe.edu.upc.travelmatch.profiles.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoriteByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoritesByExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoritesByUserIdQuery;

/** FavoriteQueryService contract. */
public interface FavoriteQueryService {
  /** Handle. */
  List<Favorite> handle(GetFavoritesByUserIdQuery query);

  /** Handle. */
  List<Favorite> handle(GetFavoritesByExperienceIdQuery query);

  /** Handle. */
  Optional<Favorite> handle(GetFavoriteByUserIdAndExperienceIdQuery query);
}
