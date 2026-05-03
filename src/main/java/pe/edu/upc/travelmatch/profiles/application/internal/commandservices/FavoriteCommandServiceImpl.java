package pe.edu.upc.travelmatch.profiles.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateFavoriteCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.DeleteFavoriteCommand;
import pe.edu.upc.travelmatch.profiles.domain.services.FavoriteCommandService;
import pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories.FavoriteRepository;

@Service
public class FavoriteCommandServiceImpl implements FavoriteCommandService {
    private final FavoriteRepository favoriteRepository;
    private final ExternalIamService externalIamService;
    private final ExternalExperienceService externalExperiencesService;

    public FavoriteCommandServiceImpl(
            FavoriteRepository favoriteRepository,
            ExternalIamService externalIamService,
            ExternalExperienceService externalExperiencesService) {
        this.favoriteRepository = favoriteRepository;
        this.externalIamService = externalIamService;
        this.externalExperiencesService = externalExperiencesService;
    }

    @Override
    public Long handle(CreateFavoriteCommand command) {
        if(!externalIamService.existsUserById(command.userId()))
            throw new IllegalArgumentException("User with id " + command.userId().userId()+ " not found");
        if(!externalExperiencesService.existsExperienceById(command.experienceId()))
            throw new IllegalArgumentException("Experience with id " + command.experienceId().experienceId() + " not found");
        var favorite = new Favorite(command.userId(), command.experienceId());
        favoriteRepository.save(favorite);
        return favorite.getId();
    }

    @Override
    public void handle(DeleteFavoriteCommand command) {
        var existingFavorite = favoriteRepository.findByUserIdAndExperienceId(command.userId(), command.experienceId());
        if(existingFavorite.isEmpty())
            throw new IllegalArgumentException("Favorite not found for user with id " + command.userId().userId() + " and experience with id " + command.experienceId().experienceId());
        favoriteRepository.delete(existingFavorite.get());
    }
}
