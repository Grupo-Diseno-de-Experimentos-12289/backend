package pe.edu.upc.travelmatch.profiles.domain.services;

import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateFavoriteCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.commands.DeleteFavoriteCommand;

/** FavoriteCommandService contract. */
public interface FavoriteCommandService {
  /** Handle. */
  Long handle(CreateFavoriteCommand command);

  /** Handle. */
  void handle(DeleteFavoriteCommand command);
}
