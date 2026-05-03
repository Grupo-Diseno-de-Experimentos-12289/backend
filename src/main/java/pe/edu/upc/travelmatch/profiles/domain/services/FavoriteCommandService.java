package pe.edu.upc.travelmatch.profiles.domain.services;

import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateFavoriteCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.DeleteFavoriteCommand;

public interface FavoriteCommandService {
    Long handle(CreateFavoriteCommand command);
    void handle(DeleteFavoriteCommand command);
}
