package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedCategoriesCommand;

public interface CategoryCommandService {
    void handle(SeedCategoriesCommand command);
}
