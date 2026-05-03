package pe.edu.upc.travelmatch.profiles.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoriteByUserIdAndExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoritesByExperienceIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GetFavoritesByUserIdQuery;
import pe.edu.upc.travelmatch.profiles.domain.services.FavoriteQueryService;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.FavoriteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteQueryServiceImpl implements FavoriteQueryService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteQueryServiceImpl(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public List<Favorite> handle(GetFavoritesByUserIdQuery query) {
        return this.favoriteRepository.findAllByUserId(query.userId());
    }

    @Override
    public List<Favorite> handle(GetFavoritesByExperienceIdQuery query) {
        return this.favoriteRepository.findAllByExperienceId(query.experienceId());
    }

    @Override
    public Optional<Favorite> handle(GetFavoriteByUserIdAndExperienceIdQuery query) {
        return this.favoriteRepository.findByUserIdAndExperienceId(query.userId(), query.experienceId());
    }
}
